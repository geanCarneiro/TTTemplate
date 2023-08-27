package main.utils;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

import javax.swing.UIManager;

public class FXProgressMonitor {

    public static final int DEFAULT_MILLIS_TO_DECIDE_POPUP = 500;
    public static final int DEFAULT_MILLIS_TO_POPUP = 2000;

    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 100;

    private static final int VBOX_SPACING = 10;
    private static final int PROGRESS_BAR_WIDTH = 300;

    private final Window window;
    private Dialog<ButtonType> dialog;
    private Label noteLabel;
    private ProgressBar myBar;

    private final String message;
    private String note;
    private boolean canceled;

    private final long T0;
    private int millisToDecideToPopup;
    private int millisToPopup;

    private int min;
    private int max;

    public FXProgressMonitor(Window window, String message, String note, int min, int max) {
        this.window = window;

        this.message = message;
        this.note = note;
        this.canceled = false;

        this.T0 = System.currentTimeMillis();
        this.millisToDecideToPopup = DEFAULT_MILLIS_TO_DECIDE_POPUP;
        this.millisToPopup = DEFAULT_MILLIS_TO_POPUP;

        this.min = min;
        this.max = max;
    }

    private double getProgress(int nv) {
        return (nv - this.min) / (double)(this.max - this.min);
    }

    /**
     * Indicate the progress of the operation being monitored. If the specified
     * value is &gt;= the maximum, the progress monitor is closed.
     *
     * @param nv an int specifying the current value, between the maximum and
     * minimum specified for this component
     * @see #setMinimum
     * @see #setMaximum
     * @see #close
     */
    public void setProgress(int nv) {
        if (nv >= this.max) {
            close();
        } else {
            if (this.myBar != null) {
                double progress = getProgress(nv);
                this.myBar.setProgress(progress);
            } else {
                long T = System.currentTimeMillis();
                long dT = (int)(T - this.T0);
                if (dT >= this.millisToDecideToPopup) {
                    int predictedCompletionTime;
                    if (nv > this.min) {
                        predictedCompletionTime = (int)(dT * (this.max - this.min) / (nv - this.min));
                    } else {
                        predictedCompletionTime = this.millisToPopup;
                    }
                    if (predictedCompletionTime >= this.millisToPopup) {
                        if (this.note != null) {
                            this.noteLabel = new Label(this.note);
                        }

                        double progress = getProgress(nv);

                        this.myBar = new ProgressBar(progress);
                        this.myBar.setPrefWidth(PROGRESS_BAR_WIDTH);

                        this.dialog = new Dialog<>();
                        this.dialog.initOwner(this.window);
                        this.dialog.initModality(Modality.NONE);

                        String dialogTitle = UIManager.getString("ProgressMonitor.progressText");
                        this.dialog.setTitle(dialogTitle);

                        ObjectProperty<ButtonType> resultProperty = this.dialog.resultProperty();
                        resultProperty.addListener((observable, oldValue, newValue) -> {
                            canceled = true;
                        });
                        this.dialog.setHeaderText(this.message);

                        VBox box = new VBox(VBOX_SPACING, this.noteLabel, this.myBar);

                        DialogPane dialogPane = this.dialog.getDialogPane();
                        dialogPane.setContent(box);

                        ObservableList<ButtonType> buttonTypes = dialogPane.getButtonTypes();
                        buttonTypes.add(ButtonType.CANCEL);

                        this.dialog.show();
                    }
                }
            }
        }
    }

    /**
     * Indicate that the operation is complete. This happens automatically when
     * the value set by setProgress is &gt;= max, but it may be called earlier
     * if the operation ends early.
     */
    public void close() {
        if (this.dialog != null) {
            this.dialog.close();
            this.dialog = null;
            this.myBar = null;
        }
    }

    /**
     * Returns the minimum value -- the lower end of the progress value.
     *
     * @return an int representing the minimum value
     * @see #setMinimum
     */
    public int getMinimum() {
        return this.min;
    }

    /**
     * Specifies the minimum value.
     *
     * @param min an int specifying the minimum value
     * @see #getMinimum
     */
    public void setMinimum(int min) {
        this.min = min;
    }

    /**
     * Returns the maximum value -- the higher end of the progress value.
     *
     * @return an int representing the maximum value
     * @see #setMaximum
     */
    public int getMaximum() {
        return this.max;
    }

    /**
     * Specifies the maximum value.
     *
     * @param max an int specifying the maximum value
     * @see #getMaximum
     */
    public void setMaximum(int max) {
        this.max = max;
    }

    /**
     * @return true if the user hits the Cancel button in the progress dialog.
     */
    public boolean isCanceled() {
        return this.canceled;
    }

    /**
     * Specifies the amount of time to wait before deciding whether or not to
     * popup a progress monitor.
     *
     * @param millisToDecideToPopup an int specifying the time to wait, in
     * milliseconds
     * @see #getMillisToDecideToPopup
     */
    public void setMillisToDecideToPopup(int millisToDecideToPopup) {
        this.millisToDecideToPopup = millisToDecideToPopup;
    }

    /**
     * Returns the amount of time this object waits before deciding whether or
     * not to popup a progress monitor.
     *
     * @see #setMillisToDecideToPopup
     */
    public int getMillisToDecideToPopup() {
        return this.millisToDecideToPopup;
    }

    /**
     * Specifies the amount of time it will take for the popup to appear.
     * (If the predicted time remaining is less than this time, the popup
     * won't be displayed.)
     *
     * @param millisToPopup  an int specifying the time in milliseconds
     * @see #getMillisToPopup
     */
    public void setMillisToPopup(int millisToPopup) {
        this.millisToPopup = millisToPopup;
    }

    /**
     * Returns the amount of time it will take for the popup to appear.
     *
     * @see #setMillisToPopup
     */
    public int getMillisToPopup() {
        return this.millisToPopup;
    }

    /**
     * Specifies the additional note that is displayed along with the
     * progress message. Used, for example, to show which file the
     * is currently being copied during a multiple-file copy.
     *
     * @param note  a String specifying the note to display
     * @see #getNote
     */
    public void setNote(String note) {
        this.note = note;
        if (this.noteLabel != null) {
            this.noteLabel.setText(note);
        }
    }

    /**
     * Returns the additional note that is displayed along with the
     * progress message.
     *
     * @return a String specifying the note to display
     * @see #setNote
     */
    public String getNote() {
        return this.note;
    }
}
