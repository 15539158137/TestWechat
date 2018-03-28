package com.dxxx.testwechatmoney;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibo on 2018/3/28.
 */
//微信版本6.65
public class MoneyService extends AccessibilityService {
    /**
     *
     * @param accessibilityEvent
     * 两种情况：第一种在桌面或者其他APP的时候：先根据notification跳转到聊天页面，获取所有红包的item，遍历出没有抢的红包item，模拟点击进行抢，弹出开的页面，再模拟点击开，再点击返回
     * 第二种：直接在聊天页面，不用通过notifaction跳转了，后面相同
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.e("页面信息", accessibilityEvent.getClassName().toString());
        Log.e("收到通知", "====");
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        //if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
//android.app.Notification
        if (accessibilityEvent.getClassName().toString().equals("android.app.Notification")) {
            Log.e("是同志", "==");
            Notification notification = (Notification) accessibilityEvent.getParcelableData();
            try {
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            return;
        }


        ;

        if (accessibilityNodeInfo != null) {
            //bpe---a8t
//com.tencent.mm:id/ad_ ====红包item的id，领过和没领过的都是这个id
            List<AccessibilityNodeInfo> nodeInfos = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ad_");

            //没有领取红包的item
            List<AccessibilityNodeInfo> noGet = new ArrayList<>();

            if (nodeInfos != null && nodeInfos.size() != 0) {
                for (AccessibilityNodeInfo accessibilityNodeInfo1 : nodeInfos) {

                    List<AccessibilityNodeInfo> list = accessibilityNodeInfo1.findAccessibilityNodeInfosByText("领取红包");
                    if (list != null && list.size() > 0) {
                        Log.e("发现一个没有领取的", "===");
                        noGet.add(accessibilityNodeInfo1);
                    }

                }

                if (noGet != null && noGet.size() > 0) {
                    //for循环是领取所有的，但是实际有效的只有第一个
                    //accessibilityNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AccessibilityNodeInfo accessibilityNodeInfo11 = noGet.get(noGet.size() - 1);
                    Log.e("触犯获取红包的逻辑", "===");
                    accessibilityNodeInfo11.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                return;
            }


            //点击了之后，打开了，但是已经领了
            List<AccessibilityNodeInfo> nodeInfos1 = accessibilityNodeInfo.findAccessibilityNodeInfosByText("已存入零钱");
            if (nodeInfos1 != null && nodeInfos1.size() != 0) {
                //表示已经领过了
                List<AccessibilityNodeInfo> back = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hx");
                if (back != null && back.size() != 0) {
                    Log.e("触犯点击,返回", "===");
                    back.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                return;
            }

            //com.tencent.mm:id/c4q==开的按钮
            List<AccessibilityNodeInfo> nodeInfos2 = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/c4q");
            if (nodeInfos2 != null && nodeInfos2.size() != 0) {
                for (AccessibilityNodeInfo accessibilityNodeInfo1 : nodeInfos2) {
                    accessibilityNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                return;
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
