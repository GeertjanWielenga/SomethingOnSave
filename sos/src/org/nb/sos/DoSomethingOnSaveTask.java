package org.nb.sos;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class DoSomethingOnSaveTask implements OnSaveTask {

    private final Document document;

    private final AtomicBoolean canceled = new AtomicBoolean();

    public DoSomethingOnSaveTask(Document doc) {
        this.document = doc;
    }

    @Override
    public void performTask() {
        FileObject fo = getFileObject(document);
        String foName = fo.getNameExt();
        execute(getFileObject(document));
        StatusDisplayer.getDefault().setStatusText("did something on save to " + foName, 10);
    }

    public FileObject getFileObject(Document doc) {
        Object sdp = doc.getProperty(Document.StreamDescriptionProperty);
        if (sdp instanceof FileObject) {
            return (FileObject) sdp;
        }
        if (sdp instanceof DataObject) {
            return ((DataObject) sdp).getPrimaryFile();
        }
        return null;
    }

    public Integer execute(FileObject fo) {
        Integer get = null;
        ExecutionDescriptor descriptor = new ExecutionDescriptor().
                showProgress(true).
                frontWindow(true).
                controllable(true);
//        ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(path)
//                .addArgument("-o").addArgument(fo.getParent().getPath() + "/" + fo.getName() + ".dart.js")
//                .addArgument("--minify")
//                .addArgument(fo.getPath());
//        ExecutionService service = ExecutionService.newService(processBuilder, descriptor, path);
//        Future<Integer> task = service.run();
//        try {
//            get = task.get();
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (ExecutionException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return get;
    }

    @Override
    public void runLocked(Runnable r) {
        r.run();
    }

    @Override
    public boolean cancel() {
        canceled.set(true);
        return true;
    }

    @MimeRegistration(mimeType = "text/javascript", service = OnSaveTask.Factory.class, position = 1500)
    public static final class FactoryImpl implements Factory {
        @Override
        public OnSaveTask createTask(Context context) {
            return new DoSomethingOnSaveTask(context.getDocument());
        }
    }

}
