package com.example.vlad.player.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;


public class OpenFileDialog extends AlertDialog.Builder {

    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files = new ArrayList<File>();
    private TextView title;
    private ListView listView;
    private FilenameFilter filenameFilter;
    private int selectedIndex = -1;
    private OpenDialogListener listener;
    private Drawable folderIcon;
    private Drawable fileIcon;
    private String accessDeniedMessage;

    public interface OpenDialogListener {
        void onSelectedFile(String fileName);
    }

    private class FileAdapter extends ArrayAdapter<File> {

        public FileAdapter(Context context, List<File> files) {
            super(context, android.R.layout.simple_list_item_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            File file = this.getItem(position);
            if (view != null) {
                view.setText(file.getName());
                if (file.isDirectory()) {
                    this.setDrawable(view, OpenFileDialog.this.folderIcon);
                } else {
                    this.setDrawable(view, OpenFileDialog.this.fileIcon);
                    if (OpenFileDialog.this.selectedIndex == position)
                        view.setBackgroundColor(this.getContext().getResources().getColor(android.R.color.holo_blue_dark));
                    else
                        view.setBackgroundColor(this.getContext().getResources().getColor(android.R.color.transparent));
                }
            }
            return view;
        }

        private void setDrawable(TextView view, Drawable drawable) {
            if (view != null) {
                if (drawable != null) {
                    drawable.setBounds(0, 0, 60, 60);
                    view.setCompoundDrawables(drawable, null, null, null);
                } else {
                    view.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

    public OpenFileDialog(Context context) {
        super(context);
        this.title = this.createTitle(context);
        this.changeTitle();
        LinearLayout linearLayout = this.createMainLayout(context);
        linearLayout.addView(this.createBackItem(context));
        this.listView = this.createListView(context);
        linearLayout.addView(this.listView);
        this.setCustomTitle(this.title)
                .setView(linearLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (OpenFileDialog.this.selectedIndex > -1 && OpenFileDialog.this.listener != null) {
                            OpenFileDialog.this.listener.onSelectedFile(OpenFileDialog.this.listView.getItemAtPosition(OpenFileDialog.this.selectedIndex).toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
    }

    @Override
    public AlertDialog show() {
        this.files.addAll(this.getFiles(this.currentPath));
        this.listView.setAdapter(new FileAdapter(this.getContext(), this.files));
        return super.show();
    }

    public OpenFileDialog setFilter(final String filter) {
        this.filenameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File file, String fileName) {
                File tempFile = new File(String.format("%s/%s", file.getPath(), fileName));
                if (tempFile.isFile())
                    return tempFile.getName().matches(filter);
                return true;
            }
        };
        return this;
    }

    public OpenFileDialog setOpenDialogListener(OpenDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public OpenFileDialog setFolderIcon(Drawable drawable) {
        this.folderIcon = drawable;
        return this;
    }

    public OpenFileDialog setFileIcon(Drawable drawable) {
        this.fileIcon = drawable;
        return this;
    }

    public OpenFileDialog setAccessDeniedMessage(String message) {
        this.accessDeniedMessage = message;
        return this;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context) {
        Point screeSize = new Point();
        getDefaultDisplay(context).getSize(screeSize);
        return screeSize;
    }

    private static int getLinearLayoutMinHeight(Context context) {
        return getScreenSize(context).y;
    }

    private LinearLayout createMainLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setMinimumHeight(getLinearLayoutMinHeight(context));
        return linearLayout;
    }

    private int getItemHeight(Context context) {
        TypedValue value = new TypedValue();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeightSmall, value, true);
        getDefaultDisplay(context).getMetrics(metrics);
        return (int) TypedValue.complexToDimension(value.data, metrics);
    }

    private TextView createTextView(Context context, int style) {
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        int itemHeight = this.getItemHeight(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        textView.setMinHeight(itemHeight);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15, 0, 0, 0);
        return textView;
    }

    private TextView createTitle(Context context) {
        TextView textView = this.createTextView(context, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        return textView;
    }

    private TextView createBackItem(Context context) {
        TextView textView = this.createTextView(context, android.R.style.TextAppearance_DeviceDefault_Small);
        Drawable drawable = this.getContext().getResources().getDrawable(android.R.drawable.ic_menu_directions);
        drawable.setBounds(0, 0, 60, 60);
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                File file = new File(OpenFileDialog.this.currentPath);
                File parentDirectory = file.getParentFile();
                if (parentDirectory != null) {
                    OpenFileDialog.this.currentPath = parentDirectory.getPath();
                    OpenFileDialog.this.RebuildFiles(((FileAdapter) OpenFileDialog.this.listView.getAdapter()));
                }
            }
        });
        return textView;
    }

    public int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width() + 80;
    }

    private void changeTitle() {
        String titleText = this.currentPath;
        int screenWidth = getScreenSize(this.getContext()).x;
        int maxWidth = (int) (screenWidth * 0.99);
        if (this.getTextWidth(titleText, this.title.getPaint()) > maxWidth) {
            while (this.getTextWidth("..." + titleText, this.title.getPaint()) > maxWidth) {
                int start = titleText.indexOf("/", 2);
                if (start > 0)
                    titleText = titleText.substring(start);
                else
                    titleText = titleText.substring(2);
            }
            this.title.setText("..." + titleText);
        } else {
            this.title.setText(titleText);
        }
    }

    private List<File> getFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] list = directory.listFiles(this.filenameFilter);
        if(list == null)
            list = new File[]{};
        List<File> fileList = Arrays.asList(list);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && file2.isFile())
                    return -1;
                else if (file.isFile() && file2.isDirectory())
                    return 1;
                else
                    return file.getPath().compareTo(file2.getPath());
            }
        });
        return fileList;
    }

    private void RebuildFiles(ArrayAdapter<File> adapter) {
        try {
            List<File> fileList = this.getFiles(this.currentPath);
            this.files.clear();
            this.selectedIndex = -1;
            this.files.addAll(fileList);
            adapter.notifyDataSetChanged();
            this.changeTitle();
        } catch (NullPointerException e) {
            String message = this.getContext().getResources().getString(android.R.string.unknownName);
            if (!this.accessDeniedMessage.equals(""))
                message = this.accessDeniedMessage;
            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private ListView createListView(Context context) {
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(index);
                assert file != null;
                if (file.isDirectory()) {
                    OpenFileDialog.this.currentPath = file.getPath();
                    OpenFileDialog.this.RebuildFiles(adapter);
                } else {
                    if (index != OpenFileDialog.this.selectedIndex)
                        OpenFileDialog.this.selectedIndex = index;
                    else
                        OpenFileDialog.this.selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return listView;
    }
}