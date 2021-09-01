package com.great.topnote.adapter;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.great.topnote.R;
import com.great.topnote.data.DownloadFile;
import com.great.topnote.data.Note;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    Context context;
    List<Note> NoteList;
    List<Integer>background;
    public NoteAdapter(Context context, List<Note> NoteList) {
        this.context = context;
        this.NoteList = NoteList;
        background=new ArrayList<>();
        background.add(R.drawable.shape1);
        background.add(R.drawable.shape2);
        background.add(R.drawable.shape5);
        background.add(R.drawable.shape3);
        background.add(R.drawable.shape4);
        background.add(R.drawable.shape6);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.note, parent, false);
        // lets create a recyclerview row item layout file
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {


        holder.image.setImageDrawable(null);
        holder.file.setImageDrawable(null);
        holder.mic.setImageDrawable(null);


        Note note =NoteList.get(NoteList.size()-(1+position));

        holder.title.setText(note.getTitle());
        holder.details.setText(note.getDetails()
                .substring(0,Math.min(note.getDetails().length(),200))
                +(note.getDetails().length()>200?" ...":""));

        holder.item.setBackgroundResource(background.get(position%background.size()));
        if(note.getImage()>0){
            if(downloadFile.getLocalFile(note.getImage()+"").exists()){
                String imageUri = downloadFile.getLocalFile(note.getImage()+"").getPath();

                Bitmap myBitmap = BitmapFactory.decodeFile(new File(imageUri).getAbsolutePath());

                holder.image.setImageBitmap(myBitmap);
            }else{
                try {
                    downloadFile.setLocalFile(note.getImage()+"",context,holder.image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(note.getSound()>0){

            holder.mic.setImageResource(R.drawable.ic_mic_blue_24dp);
            holder.mic.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(downloadFile.getLocalFile(note.getSound()+"").exists()){
                        String soundUri = Environment.getExternalStorageDirectory() + "/"+"Top-Task/";
                        audioPlayer(soundUri,note.getSound()+"");
                    }
                    else{
                        try {
                            downloadFile.setLocalFile(note.getSound()+"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        if(note.getFile()>0){
            holder.file.setImageResource(R.drawable.ic_file_blue_24dp);
            holder.file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        OpenPDFFile(note.getFile()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void audioPlayer(String path, String fileName){
        MediaPlayer mp = new MediaPlayer();
        try {
            Dialog NoteTask = new Dialog((Context) context);
            NoteTask.setContentView(R.layout.voiceshap);
            NoteTask.show();
            ProgressBar progressBar =NoteTask.findViewById(R.id.progressBar);
            ImageView play =NoteTask.findViewById(R.id.play),
                    replay =NoteTask.findViewById(R.id.replay);
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();
            final boolean[] show = {false};
            steekPostion(mp,progressBar,show);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(show[0])
                        mp.start();
                    else
                        mp.pause();
                    show[0] =!show[0];
                }
            });
            replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.seekTo(0);
                    mp.start();
                    steekPostion(mp,progressBar,show);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void steekPostion(MediaPlayer mp ,ProgressBar progressBar ,boolean[]show){
        new Thread(new Runnable (){
            AtomicBoolean stop = new AtomicBoolean(false);

            public void stop() {
                stop.set(true);
                show[0]=true;
            }

            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int len=0,val=0;
                while (!stop.get()) {
                    int position=(len*100)/mp.getDuration();
                    if(position>=100){
                        position=100;
                        stop();
                    }
                    progressBar.setProgress(position);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(val==0)
                        len=200;
                    else
                        len+=200;
                    val++;
                }
            }
        }).start();

    }
    public void OpenPDFFile(String Node) throws IOException {
        File pdfFile =
                new File(Environment.getExternalStorageDirectory()+ "/"+"Top-Task/",""+Node);//File path
        if (pdfFile.exists()) //Checking for the file is exist or not
        {
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenintent.setDataAndType(path, "application/pdf");
            context.startActivity(pdfOpenintent);//Staring the pdf viewer
        }
        else{
            downloadFile.setLocalStringfile(Node);
        }
    }
    DownloadFile downloadFile=new DownloadFile();
    @Override
    public int getItemCount() {
        return NoteList.size();
    }


    public static final class NoteHolder extends RecyclerView.ViewHolder{


        TextView title,details;
        ImageView mic,file,image;
        ConstraintLayout item;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            details=itemView.findViewById(R.id.details);
            mic=itemView.findViewById(R.id.mic);
            item=itemView.findViewById(R.id.note);
            file=itemView.findViewById(R.id.file);
            image=itemView.findViewById(R.id.image);
        }
    }

}
