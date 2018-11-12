package com.android.launcher3.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.SparseArray;

import com.android.launcher3.Application.MainApplication;
import com.android.launcher3.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abylee on 2018/1/30.
 */

public class TasksManager {
    private final static class HolderClass {
        private final static TasksManager INSTANCE
                = new TasksManager();
    }

    public static TasksManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private TasksManagerDBController dbController;
    private List<TasksManagerModel> modelList;

    private TasksManager() {
        dbController = new TasksManagerDBController();
        modelList = dbController.getAllTasks();
    }


    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    public void addTaskForView(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForView(final int id) {
        taskSparseArray.remove(id);
    }

    public void releaseTask() {
        taskSparseArray.clear();
    }

    private FileDownloadConnectListener listener;

    private void registerServiceConnectionListener(FileDownloadConnectListener listener) {
        if (this.listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(this.listener);
        }

        this.listener = listener;
        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void onCreate(FileDownloadConnectListener listener) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(listener);
        }
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public TasksManagerModel get(final int position) {
        return modelList.get(position);
    }

    public TasksManagerModel getById(final int id) {
        for (TasksManagerModel model : modelList) {
            if (model.getId() == id) {
                return model;
            }
        }

        return null;
    }

    /**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     */
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    public TasksManagerModel addTask(final String url) {
        return addTask(url, createPath(url));
    }

    public TasksManagerModel addTask(final String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        final int id = FileDownloadUtils.generateId(url, path);
        TasksManagerModel model = getById(id);
        if (model != null) {
            return model;
        }
        final TasksManagerModel newModel = dbController.addTask(url, path);
        if (newModel != null) {
            modelList.add(newModel);
        }

        return newModel;
    }

    public String createPath(String name) {
        return FileDownloadUtils.getDefaultSaveRootPath() + "/" + name + ".apk";
    }

    private static class TasksManagerDBController {
        public final static String TABLE_NAME = "tasksmanger";
        private final SQLiteDatabase db;

        private TasksManagerDBController() {
            TasksManagerDBOpenHelper openHelper = new TasksManagerDBOpenHelper(MainApplication.getContext());

            db = openHelper.getWritableDatabase();
        }

        public List<TasksManagerModel> getAllTasks() {
            final Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            final List<TasksManagerModel> list = new ArrayList<>();
            try {
                if (!c.moveToLast()) {
                    return list;
                }

                do {
                    TasksManagerModel model = new TasksManagerModel();
                    model.setId(c.getInt(c.getColumnIndex(TasksManagerModel.ID)));
                    model.setName(c.getString(c.getColumnIndex(TasksManagerModel.NAME)));
                    model.setUrl(c.getString(c.getColumnIndex(TasksManagerModel.URL)));
                    model.setPath(c.getString(c.getColumnIndex(TasksManagerModel.PATH)));
                    list.add(model);
                } while (c.moveToPrevious());
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            return list;
        }

        public TasksManagerModel addTask(final String url, final String path) {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
                return null;
            }

            // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
            final int id = FileDownloadUtils.generateId(url, path);

            TasksManagerModel model = new TasksManagerModel();
            model.setId(id);
            model.setName(MainApplication.getContext().getString(R.string.tasks_manager_name, id));
            model.setUrl(url);
            model.setPath(path);

            final boolean succeed = db.insert(TABLE_NAME, null, model.toContentValues()) != -1;
            return succeed ? model : null;
        }


    }

    // ----------------------- model
    private static class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
        public final static String DATABASE_NAME = "tasksmanager.db";
        public final static int DATABASE_VERSION = 2;

        public TasksManagerDBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TasksManagerDBController.TABLE_NAME
                    + String.format(
                    "("
                            + "%s INTEGER PRIMARY KEY, " // id, download id
                            + "%s VARCHAR, " // name
                            + "%s VARCHAR, " // url
                            + "%s VARCHAR " // path
                            + ")"
                    , TasksManagerModel.ID
                    , TasksManagerModel.NAME
                    , TasksManagerModel.URL
                    , TasksManagerModel.PATH

            ));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.delete(TasksManagerDBController.TABLE_NAME, null, null);
            }
        }
    }

    private static class TasksManagerModel {
        public final static String ID = "id";
        public final static String NAME = "name";
        public final static String URL = "url";
        public final static String PATH = "path";

        private int id;
        private String name;
        private String url;
        private String path;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public ContentValues toContentValues() {
            ContentValues cv = new ContentValues();
            cv.put(ID, id);
            cv.put(NAME, name);
            cv.put(URL, url);
            cv.put(PATH, path);
            return cv;
        }
    }
}
