package com.saas.saasuser.view.timeselect;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.saas.saasuser.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期picker相关
 */
public class TimeSelector {
    public interface ResultHandler {
        void handle(String time);
    }

    public enum SCROLLTYPE {
        YEAR(1),
        MONTH(2),
        DAY(4),
        HOUR(8),
        MINUTE(16);


        SCROLLTYPE(int value) {
            this.value = value;
        }
        public int value;

    }

    private int scrollUnits = SCROLLTYPE.YEAR.value + SCROLLTYPE.MONTH.value + SCROLLTYPE.DAY.value + SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value ;
    private ResultHandler handler;
    private Context context;
    private final String FORMAT_STR = "yyyy-MM-dd HH:mm";

    private Dialog seletorDialog;
    private PickerView year_pv;
    private PickerView month_pv;
    private PickerView day_pv;
    private PickerView hour_pv;
    private PickerView minute_pv, second_pv;

    private final int MAXMINUTE = 59;
    private final int MAXSECOND = 59;
    private int MAXHOUR = 23;
    private final int MINMINUTE = 0;
    private int MINHOUR = 0;
    private final int MAXMONTH = 12;

    private int curYear;
    private int curMonth;
    private int curDay;
    private int curHour;
    private int curMininute, curSecond;
    private TextView tvDate;

    private ArrayList<String> year, month, day, hour, minute, second;
    private int startYear, startMonth, startDay, startHour, startMininute, startSecond, endYear, endMonth, endDay, endHour, endMininute, endSecond, minute_workStart, minute_workEnd, second_wordStart, second_workEnd, hour_workStart, hour_workEnd;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin, spanSecond;
    private Calendar selectedCalender = Calendar.getInstance();
    private final long ANIMATORDELAY = 200L;
    private final long CHANGEDELAY = 90L;
    private Calendar startCalendar;
    private Calendar curCalendar;
    private Calendar endCalendar;
    private TextView tv_cancle;
    private TextView tv_select;

    /**
     * @param context
     * @param resultHandler 选取时间后回调
     * @param startDate     format："yyyy-MM-dd HH:mm:ss"
     * @param endDate
     */
    public TimeSelector(Context context, ResultHandler resultHandler, String startDate, String endDate) {
        this.context = context;
        this.handler = resultHandler;
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtils.parse(startDate, FORMAT_STR));
        endCalendar.setTime(DateUtils.parse(endDate, FORMAT_STR));
        initDialog();
        initView();
    }

    /**
    * @Description: 显示时间控件
    */
    public void show(final TextView tvDate) {
        this.tvDate = tvDate;
        curCalendar = Calendar.getInstance();
        //如果没有选择时间，时间控件默认选中当前时间
        if (tvDate.getText().toString().isEmpty()) {
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_STR);
            String str = formatter.format(curDate);
            curCalendar.setTime(DateUtils.parse(str, FORMAT_STR));
        } else {
            curCalendar.setTime(DateUtils.parse(tvDate.getText().toString(), FORMAT_STR));
        }
        initParameter();
        initTimer();
        addListener();
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDate.setText(DateUtils.format(selectedCalender.getTime(), FORMAT_STR));
                seletorDialog.dismiss();
            }
        });
        seletorDialog.show();
    }
    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(context, R.style.time_dialog);
            seletorDialog.setCancelable(false);
            seletorDialog.setCanceledOnTouchOutside(true);//设置点击其他区域消失
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seletorDialog.setContentView(R.layout.datetime_dialog_selector);
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getInstance(context).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        year_pv = (PickerView) seletorDialog.findViewById(R.id.year_pv);
        month_pv = (PickerView) seletorDialog.findViewById(R.id.month_pv);
        day_pv = (PickerView) seletorDialog.findViewById(R.id.day_pv);
        hour_pv = (PickerView) seletorDialog.findViewById(R.id.hour_pv);
        minute_pv = (PickerView) seletorDialog.findViewById(R.id.minute_pv);

        tv_cancle = (TextView) seletorDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) seletorDialog.findViewById(R.id.tv_select);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletorDialog.dismiss();
            }
        });

    }

    private void initParameter() {
        curYear = curCalendar.get(Calendar.YEAR);
        curMonth = curCalendar.get(Calendar.MONTH) + 1 ;
        curDay = curCalendar.get(Calendar.DAY_OF_MONTH);
        curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
        curMininute = curCalendar.get(Calendar.MINUTE);
        curSecond = curCalendar.get(Calendar.SECOND);
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMininute = startCalendar.get(Calendar.MINUTE);
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMininute = endCalendar.get(Calendar.MINUTE);
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMininute != endMininute);
        spanSecond = (!spanMin) && (startSecond != endSecond);
        selectedCalender.setTime(curCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
            for (int i = startDay; i <= curCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
            for (int i = startHour; i <= MAXHOUR; i++) {
                hour.add(fomatTimeUnit(i));
            }
            for (int i = startMininute; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(fomatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
            for (int i = startHour; i <= MAXHOUR; i++) {
                hour.add(fomatTimeUnit(i));
            }
            for (int i = startMininute; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(fomatTimeUnit(i));
            }
            for (int i = startHour; i <= MAXHOUR; i++) {
                hour.add(fomatTimeUnit(i));
            }
            for (int i = startMininute; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            day.add(fomatTimeUnit(startDay));
            for (int i = startHour; i <= endHour; i++) {
                hour.add(fomatTimeUnit(i));
            }
            for (int i = startMininute; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            day.add(fomatTimeUnit(startDay));
            hour.add(fomatTimeUnit(startHour));
            for (int i = startMininute; i <= endMininute; i++) {
                minute.add(fomatTimeUnit(i));
            }
        }
        loadComponent();

    }
    /**
    * @author
    * @Title:  fomatTimeUnit
    * @Description: 格式化显示时间
    * @date 2017/3/1 19:23
    */
    private String fomatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        if (second == null) second = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
        second.clear();
    }


    private void addListener() {
        year_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                //设置年
                year_pv.setmCurrentYearSelected(Integer.parseInt(text));
                //滑动“年”view时动态刷新其他日期控件
                monthChange();
            }
        });
        month_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                //设置当前选择月
                month_pv.setmCurrentMonthSelected(Integer.parseInt(text));
                //设置月份之前将日期置成1号，避免选择日期过大，切换到2月没有当前日期月份自动加1
                selectedCalender.set(Calendar.DATE, 1);
                //月份值
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                dayChange();
            }
        });
        day_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
                day_pv.setmCurrentDaySelected(Integer.parseInt(text));
                curDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                hourChange();
            }
        });
        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                hour_pv.setmCurrentHourSelected(Integer.parseInt(text));
                minuteChange();
            }
        });
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
                minute_pv.setmCurrentMinSelected(Integer.parseInt(text));
            }


        });
    }

    private void setCurDate(){
        year_pv.setSelected(curYear - startYear);
        year_pv.setmCurrentYearSelected(curYear - startYear);
        month_pv.setSelected(curMonth-1);
        month_pv.setmCurrentMonthSelected(curMonth);
        day_pv.setSelected(curDay - 1);
        day_pv.setmCurrentDaySelected(curDay);
        hour_pv.setSelected(curHour);
        hour_pv.setmCurrentHourSelected(curHour);
        minute_pv.setSelected(curMininute);
        minute_pv.setmCurrentMinSelected(curMininute);
    }

    public void loadComponent() {
        year_pv.setData(year);
        month_pv.setData(month);
        day_pv.setData(day);
        hour_pv.setData(hour);
        minute_pv.setData(minute);
        setCurDate();
        excuteScroll();
    }

    private void excuteScroll() {
        year_pv.setCanScroll(year.size() > 1 && (scrollUnits & SCROLLTYPE.YEAR.value) == SCROLLTYPE.YEAR.value);
        month_pv.setCanScroll(month.size() > 1 && (scrollUnits & SCROLLTYPE.MONTH.value) == SCROLLTYPE.MONTH.value);
        day_pv.setCanScroll(day.size() > 1 && (scrollUnits & SCROLLTYPE.DAY.value) == SCROLLTYPE.DAY.value);
        hour_pv.setCanScroll(hour.size() > 1 && (scrollUnits & SCROLLTYPE.HOUR.value) == SCROLLTYPE.HOUR.value);
        minute_pv.setCanScroll(minute.size() > 1 && (scrollUnits & SCROLLTYPE.MINUTE.value) == SCROLLTYPE.MINUTE.value);
    }

    private void monthChange() {
        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(fomatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
        }
        month_pv.setData(month);
        selectedCalender.set(Calendar.MONTH, month_pv.getmCurrentMonthSelected() - 1);
        month_pv.setSelected(month_pv.getmCurrentMonthSelected() - 1);
        month_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, CHANGEDELAY);

    }
    /**
    * @author  谭亚东
    * @Title:  dayChange
    * @Description: 点击月 修改日的显示
    * @date 2017/2/27 20:27
    */
    private void dayChange() {
        day.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH);
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(fomatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
        }

        day_pv.setData(day);
        //如果上一次选择的日 大于当前日的集合，则默认显示第一天
        if (day_pv.getmCurrentDaySelected() > day.size()) {
            selectedCalender.set(Calendar.DATE, day.size() - 1);
            day_pv.setSelected(day.size() - 1);
        } else {
            //否则显示上一次选择的天
            selectedCalender.set(Calendar.DATE, day_pv.getmCurrentDaySelected());
            day_pv.setSelected(day_pv.getmCurrentDaySelected() - 1);
        }
        day_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, CHANGEDELAY);
    }

    private void hourChange() {
        hour.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);

        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
            for (int i = startHour; i <= MAXHOUR; i++) {
                hour.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
            for (int i = MINHOUR; i <= endHour; i++) {
                hour.add(fomatTimeUnit(i));
            }
        } else {

            for (int i = MINHOUR; i <= MAXHOUR; i++) {
                hour.add(fomatTimeUnit(i));
            }

        }
        hour_pv.setData(hour);
        hour_pv.setSelected(hour_pv.getmCurrentHourSelected());
        hour_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, CHANGEDELAY);

    }

    private void minuteChange() {
        minute.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
        int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);

        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
            for (int i = startMininute; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
            for (int i = MINMINUTE; i <= endMininute; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (selectedHour == hour_workStart) {
            for (int i = minute_workStart; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else if (selectedHour == hour_workEnd) {
            for (int i = MINMINUTE; i <= minute_workEnd; i++) {
                minute.add(fomatTimeUnit(i));
            }
        } else {
            for (int i = MINMINUTE; i <= MAXMINUTE; i++) {
                minute.add(fomatTimeUnit(i));
            }
        }
        minute_pv.setData(minute);
        minute_pv.setSelected(minute_pv.getmCurrentMinSelected());
        excuteScroll();
    }

    public int setScrollUnit(SCROLLTYPE... scrolltypes) {
        scrollUnits = 0;
        for (SCROLLTYPE scrolltype : scrolltypes) {
            scrollUnits ^= scrolltype.value;
        }
        return scrollUnits;
    }


}