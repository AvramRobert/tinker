package com.example.tinker.tasks;

import android.os.AsyncTask;
import com.example.tinker.MainActivity;
import com.example.tinker.Procedure;
import com.example.tinker.ProcedureOld;
import com.example.tinker.content.JobEntry;
import com.example.tinker.content.Jobs;
import com.example.tinker.handlers.MessageHandler;
import com.example.tinker.views.Views;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Uses overall new Thread creation for each complex method, without fetching one from the pool. Problem: overhead
 */
public class TinkerTaskOld {

    private Jobs[] jobs;
    private List<Jobs> allEntries;
    private Jobs necessaryEntries;
    private Map<String, StringBuilder> map;
    private MessageHandler handler = new MessageHandler(MainActivity.viewController.getView(Views.MAIN));


    public void execute(String... params) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, Void> t = new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                handler.sendToggleDialogMessage();
            }

            @Override
            protected Void doInBackground(String... params) {
                jobs = new Jobs[params.length];
                ProcedureOld p = new ProcedureOld();
                try {
                    for (int i = 0; i < params.length; i++) {
                        p.setSearchable(params[i]);
                        jobs[i] = p.fetch();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                RunnableFuture<Void> call = new FutureTask<Void>(new Callable<Void>() {
                    @Override
                    public Void call() {
                            allEntries = (params.length > 1) ? Jobs.compareLists(jobs): Arrays.asList(jobs);
                            if (allEntries.get(0).isEmpty()) {
                                handler.sendSetErrorMessage();
                            } else {
                                necessaryEntries = allEntries.get(0).removeDuplicates();
                                mapInformation();
                                handler.sendPopulateDisplayMessage();
                            }
                        return null;
                    }

                });
                new Thread(call).start();
                try {
                    call.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                handler.sendDismissDialogMessage();
            }
        };
        t.execute(params);
    }


    public void mapInformation() {
        List<Jobs> common = allEntries;
        if (common != null && !common.isEmpty()) {
            map = new HashMap<>();
            for (Jobs j : common) {
                for (JobEntry e : j.getEntries()) {
                    if (map.containsKey(e.getTitle())) {
                        map.put(e.getTitle(), map.get(e.getTitle()).append(j.getName()).append(" - ").append(e.getJob()).append("\n"));
                    } else {
                        map.put(e.getTitle(), new StringBuilder().append(j.getName()).append(" - ").append(e.getJob()).append("\n"));
                    }


                }
            }
        }
    }

    public Map<String, StringBuilder> getMappedData() {
        return this.map;
    }

    public List<JobEntry> getAllNecesarryEntries() {
        return necessaryEntries.getEntries();
    }
    public List<JobEntry> getNecessaryEntries(int start, int finish) {
        int size = necessaryEntries.getEntries().size();
        return (finish > size) ? necessaryEntries.getEntries().subList(start, size): necessaryEntries.getEntries().subList(start, finish);
    }

}
