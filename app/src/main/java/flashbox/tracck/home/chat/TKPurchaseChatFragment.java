package flashbox.tracck.home.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseFragment;
import flashbox.tracck.common.Common;
import flashbox.tracck.common.Constants;
import flashbox.tracck.model.TKChat;
import flashbox.tracck.model.TKPurchase;

public class TKPurchaseChatFragment extends TKBaseFragment implements TKEmoticonsGridAdapter.KeyClickListener {
    private static final int NO_OF_EMOTICONS = 54;

    private View popUpView;
    private ListView chatList;
    private LinearLayout parentLayout;
    private LinearLayout emoticonsCover;
    private ArrayList<TKChat> chats;
    private TKChatListAdapter mAdapter;
    private PopupWindow popupWindow;
    private int keyboardHeight;
    private EditText content;
    private boolean isKeyBoardVisible;
    private Bitmap[] emoticons;
    private Context context;

    public TKPurchaseChatFragment() {
    }

    public void setParentLayout(Context context, LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.context = context;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_purchase_chat;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initUI() {
        View view = mContentView;
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        chatList = (ListView) view.findViewById(R.id.LstChat);
        emoticonsCover = (LinearLayout)view.findViewById(R.id.footer_for_emoticons);
        popUpView = View.inflate(getContext(), R.layout.emoticons_popup, null);

        if (chats == null) {
            chats = new ArrayList<TKChat>();
            mAdapter = new TKChatListAdapter(getActivity().getApplicationContext(), chats);

            // To show all of message type
            insertDummyData();
        }
        chatList.setAdapter(mAdapter);
        chatList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                return false;
            }
        });

        Button postButton = (Button) view.findViewById(R.id.btnReturnExchange);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReturnExchange(context);
            }
        });

        //Defining default height of keyboard which is equal to 230 dip
        final float popUpheight = getResources().getDimension(
                R.dimen.keyboard_height);
        changeKeyboardHeight((int) popUpheight);

        // Showing and Dismissing pop up on clicking emoticons button
        ImageView emoticonsButton = (ImageView) view.findViewById(R.id.emoticons_button);
        emoticonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popupWindow.isShowing()) {
                    popupWindow.setHeight((int) (keyboardHeight));
                    if (isKeyBoardVisible) {
                        emoticonsCover.setVisibility(LinearLayout.GONE);
                    } else {
                        emoticonsCover.setVisibility(LinearLayout.VISIBLE);
                    }
                    popupWindow.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
                } else {
                    popupWindow.dismiss();
                }

            }
        });
        readEmoticons();
        enablePopUpView();
        checkKeyboardHeight(parentLayout);
        enableFooterView(view);
    }

    @Override
    protected void reload(Bundle bundle) {

    }

    private void showReturnExchange(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.fragment_return_exchange);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        final LinearLayout fragmentBody = (LinearLayout) dialog.findViewById(R.id.fragmentBody);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TKPurchase temp  = Common.getPurchase();

        TextView txtProductName = (TextView) dialog.findViewById(R.id.txt_productname);
        txtProductName.setText(temp.getStrProductName());

        TextView txtShopName = (TextView) dialog.findViewById(R.id.txt_showname);
        txtShopName.setText(temp.getStrShopName());

        TextView txtPeriod = (TextView) dialog.findViewById(R.id.txt_period);
        txtPeriod.setText(temp.getStrPeriod());

        Button btnStatus = (Button) dialog.findViewById(R.id.btn_status);
        btnStatus.setText(temp.getStrStatus());

        switch (temp.getStrStatus()) {
            case "En route":
                btnStatus.setBackgroundResource(R.drawable.roundyellow);
                break;
            case "Delivered":
                btnStatus.setBackgroundResource(R.drawable.roundgreen);
                break;
            case "Service":
                btnStatus.setBackgroundResource(R.drawable.roundred);
                break;
            case "Packing":
                btnStatus.setBackgroundResource(R.drawable.roundpurple);
                break;
        }

        final NumberPicker picker = (NumberPicker) fragmentBody.findViewById(R.id.pickerTimePeriod);;
        picker.setMinValue(0);
        picker.setMaxValue(Constants.arrTimePeriods.length - 1);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(Constants.arrTimePeriods);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Common.setSelectedTimePeriod(Constants.arrTimePeriods[newVal]);
            }
        });
        picker.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                float realPickerWidth = (float)(picker.getMeasuredWidth() * displayMetrics.scaledDensity);
                float pickerWidth = (float) (displayMetrics.widthPixels * 0.27);

                float scaleX = pickerWidth/realPickerWidth;
                picker.setScaleX(scaleX);
            }
        });

        final DatePicker pickerDate = (DatePicker) fragmentBody.findViewById(R.id.pickerDate);
        pickerDate.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                float realPickerWidth = (float)(pickerDate.getMeasuredWidth() * displayMetrics.scaledDensity);
                float pickerWidth = (float) (displayMetrics.widthPixels * 0.75);

                float scaleX = pickerWidth/realPickerWidth;
                pickerDate.setScaleX(scaleX);
            }
        });

        final Button btnExchange = (Button) fragmentBody.findViewById(R.id.btn_exchange);
        final Button btnReturn = (Button) fragmentBody.findViewById(R.id.btn_return);
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReturn.setBackgroundResource(R.color.colorGray);
                btnExchange.setBackgroundResource(R.color.colorGreen);
                btnExchange.setTextColor(Color.WHITE);
                btnReturn.setTextColor(Color.BLACK);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReturn.setBackgroundResource(R.color.colorGreen);
                btnExchange.setBackgroundResource(R.color.colorGray);
                btnReturn.setTextColor(Color.WHITE);
                btnExchange.setTextColor(Color.BLACK);
            }
        });

        Button btnNext = (Button) fragmentBody.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBody.removeAllViews();

                View.inflate(getContext(), R.layout.fragment_purchase_description, fragmentBody);

                TextView txtTimePeriod = (TextView) fragmentBody.findViewById(R.id.txtTimePeriod);
                String strTimePeriod = Common.getSelectedTimePeriod();
                if (strTimePeriod == null || strTimePeriod.equals(""))
                    strTimePeriod = Constants.arrTimePeriods[0];
                txtTimePeriod.setText(strTimePeriod);

                TextView btnEdit = (TextView) fragmentBody.findViewById(R.id.txtEditTimePeriod);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showReturnExchange(context);
                    }
                });

                Button btnSubmit = (Button) fragmentBody.findViewById(R.id.btnSubmit);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentBody.removeAllViews();
                        View.inflate(getContext(), R.layout.fragment_purchase_submit, fragmentBody);

                        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                        int dialogWidth = (int)(displayMetrics.widthPixels * 1);
                        int dialogHeight = (int)(displayMetrics.heightPixels * 0.75);
                        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

                        dialog.show();

                        Spanned sp = new SpannableStringBuilder("Packaging was broken");
                        TKChat chat = new TKChat();
                        chat.setIntMsgType(Constants.MSG_FEEDBACK);
                        chat.setSpanMsg(sp);
                        chat.setStrTime("11:27 am");
                        chats.add(chat);

                        Spanned sp1 = new SpannableStringBuilder("18.03.2015, 10 am- 12 pm");
                        TKChat chat1 = new TKChat();
                        chat1.setIntMsgType(Constants.MSG_SCHEDULED_CALL);
                        chat1.setSpanMsg(sp1);
                        chat1.setStrTime("12:16 pm");
                        chat1.setStrDate("18.09.2017");
                        chats.add(chat1);

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 1);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }

    private void insertDummyData() {
        Spanned sp = new SpannableStringBuilder("Order placed");
        TKChat chat1 = new TKChat();
        chat1.setIntMsgType(Constants.MSG_SENT);
        chat1.setSpanMsg(sp);
        chat1.setStrTime("10:52 am");
        chats.add(chat1);

        sp = new SpannableStringBuilder("Packing");
        TKChat chat2 = new TKChat();
        chat2.setIntMsgType(Constants.MSG_RECEIVED);
        chat2.setSpanMsg(sp);
        chat2.setStrTime("9:26 am");
        chats.add(chat2);

        sp = new SpannableStringBuilder("On the way");
        TKChat chat3 = new TKChat();
        chat3.setIntMsgType(Constants.MSG_RECEIVED);
        chat3.setSpanMsg(sp);
        chat3.setStrTime("9:26 am");
        chats.add(chat3);

        sp = new SpannableStringBuilder("Delivered");
        TKChat chat4 = new TKChat();
        chat4.setIntMsgType(Constants.MSG_RECEIVED);
        chat4.setSpanMsg(sp);
        chat4.setStrTime("10:47 am");
        chats.add(chat4);

        sp = new SpannableStringBuilder("Recieved");
        TKChat chat5 = new TKChat();
        chat5.setIntMsgType(Constants.MSG_SENT);
        chat5.setSpanMsg(sp);
        chat5.setStrTime("10:52 am");
        chats.add(chat5);

        mAdapter.notifyDataSetChanged();
    }

    private void readEmoticons() {
        emoticons = new Bitmap[NO_OF_EMOTICONS];
        for (short i=0;i<NO_OF_EMOTICONS;i++){
            emoticons[i]=getImage((i+1)+".png");
        }
    }

    /**
     * Enabling all content in footer i.e. post window
     */
    private void enableFooterView(View view) {

        content = (EditText) view.findViewById(R.id.chat_content);
        content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        Button postButton = (Button) view.findViewById(R.id.btnSend);
        postButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (content.getText().toString().length() > 0) {
                    Spanned sp = content.getText();
                    TKChat chat = new TKChat();
                    chat.setIntMsgType(Constants.MSG_SENT);
                    chat.setSpanMsg(sp);
                    chat.setStrTime("10:52 am");
                    chats.add(chat);
                    content.setText("");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Checking keyboard height and keyboard visibility
     */
    int previousHeightDiffrence = 0;
    private void checkKeyboardHeight(final View parentLayout) {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight = parentLayout.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);

                        if (previousHeightDiffrence - heightDifference > 50) {
                            popupWindow.dismiss();
                        }

                        previousHeightDiffrence = heightDifference;
                        if (heightDifference > 100) {

                            isKeyBoardVisible = true;
                            changeKeyboardHeight(heightDifference);

                        } else {

                            isKeyBoardVisible = false;

                        }

                    }
                });

    }

    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard
     *
     * @param height
     *            minimum height by which we can make sure actual keyboard is
     *            open or not
     */
    private void changeKeyboardHeight(int height) {

        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, keyboardHeight);
            emoticonsCover.setLayoutParams(params);
        }

    }

    /**
     * Defining all components of emoticons keyboard
     */
    private void enablePopUpView() {

        ViewPager pager = (ViewPager) popUpView.findViewById(R.id.emoticons_pager);
        pager.setOffscreenPageLimit(3);

        ArrayList<String> paths = new ArrayList<String>();

        for (short i = 1; i <= NO_OF_EMOTICONS; i++) {
            paths.add(i + ".png");
        }

        TKEmoticonsPagerAdapter adapter = new TKEmoticonsPagerAdapter(getActivity(), paths, this);
        pager.setAdapter(adapter);

        // Creating a pop window for emoticons keyboard
        popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT,
                (int) keyboardHeight, false);

        TextView backSpace = (TextView) popUpView.findViewById(R.id.back);
        backSpace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                content.dispatchKeyEvent(event);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                emoticonsCover.setVisibility(LinearLayout.GONE);
            }
        });
    }

    /**
     * For loading smileys from assets
     */
    private Bitmap getImage(String path) {
        AssetManager mngr = getActivity().getAssets();
        InputStream in = null;
        try {
            in = mngr.open("emoticons/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        return temp;
    }

    @Override
    public void keyClickedIndex(final String index) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                StringTokenizer st = new StringTokenizer(index, ".");
                Drawable d = new BitmapDrawable(getResources(),emoticons[Integer.parseInt(st.nextToken()) - 1]);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };

        Spanned cs = Html.fromHtml("<img src ='"+ index +"'/>", imageGetter, null);

        int cursorPosition = content.getSelectionStart();
        content.getText().insert(cursorPosition, cs);
    }

    /**
     * Overriding onKeyDown for dismissing keyboard on key down
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return false;
        } else {
//            return getActivity().onKeyDown(keyCode, event);
            return true;
        }
    }
}
