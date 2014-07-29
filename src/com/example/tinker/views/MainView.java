package com.example.tinker.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.tinker.MainActivity;
import com.example.tinker.R;
import com.example.tinker.content.JobEntry;
import com.example.tinker.tasks.TinkerTask;
import com.example.tinker.tasks.TinkerTaskOld;
import com.example.tinker.utils.StreamParser;
import com.example.tinker.views.interfaces.DisplayView;
import com.example.tinker.views.interfaces.ScrollViewListener;

import java.util.List;
import java.util.concurrent.*;
public class MainView implements DisplayView {

    private TextView searchResult;
    private EditText input;
    public ProgressDialog dialog;
    private Context context;
    private TinkerTask tinker;
    private HybridScrollView sview;
    private int currentDisplayBound = 0;
    @Override
    public int getId() {
        return R.layout.main;
    }

    @Override
    public void initialize(Activity activity) {
        Runnable mainView = new Runnable() {
            @Override
            public void run() {
                TextView expand = (TextView) activity.findViewById(R.id.expandText);
                expand.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        populateDisplay(false);
                    }
                });
                expand.setVisibility(View.INVISIBLE);
                Button searchBT = (Button) activity.findViewById(R.id.searchButton);
                searchResult = (TextView) activity.findViewById(R.id.searchResult);
                input = (EditText) activity.findViewById(R.id.inputField);
                context = activity;
                sview = (HybridScrollView) activity.findViewById(R.id.hybridScrollView);
                sview.setScrollViewListener(new ScrollViewListener() {
                    @Override
                    public void onScrollChanged(HybridScrollView scrollView, int x, int y, int oldx, int oldy) {
                        View personal = sview.getChildAt(sview.getChildCount() - 1);

                        int diff = (personal.getBottom() - (sview.getHeight() + sview.getScrollY()));

                        if (diff <= 0) {
                            expand.setVisibility(View.VISIBLE);
                        } else {
                            expand.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                searchBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search();
                    }
                });

                input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            search();
                            handled = true;
                        }
                        return handled;
                    }
                });
            }
        };
        MainActivity.threadPool.submit(mainView);
        tinker = new TinkerTask();
    }

    private void search() {
        currentDisplayBound = 0;
        Editable params = input.getText();
        input.setText("");
        if (!params.toString().isEmpty()) {
            String[] parsed = StreamParser.parse(params);
            try {
                tinker.execute(parsed);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

        public void populateDisplay(boolean reset) {
        int displayLimit = 50;
        int overallSize = tinker.getAllNecesarryEntries().size();
        if(reset) resetDisplay();
        if (currentDisplayBound <= overallSize) {
            List<JobEntry> j = tinker.getNecessaryEntries(currentDisplayBound, currentDisplayBound + displayLimit-1);
            for (JobEntry e : j) {
                setHyperlinks(e.toAnchor());
                searchResult.append("\n");
                searchResult.append(tinker.getMappedData().get(e.getTitle()));
                searchResult.append("\n");
            }
            currentDisplayBound += displayLimit;
        }
    }

    @Override
    public void setHyperlinks(String s) {
            searchResult.append(Html.fromHtml(s));
            searchResult.setMovementMethod(LinkMovementMethod.getInstance());
        }

    @Override
    public void resetDisplay() {
        searchResult.setText("");
    }

    @Override
    public void setError() {
        String error = "I am terribly sorry to inform you that I have not found any results.";
        searchResult.setText(error);
    }

    @Override
    public void toggleDialog() {
        dialog = ProgressDialog.show(context, "Please Wait..", "Data is being tinkered with!", false, false);
    }

    @Override
    public void dismissDialog() {
        if(dialog != null) dialog.dismiss();
    }
}
