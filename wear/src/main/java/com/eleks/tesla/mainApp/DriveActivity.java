package com.eleks.tesla.mainApp;

import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eleks.tesla.R;
import com.eleks.tesla.events.ToHandHoldRequestEvent;

import de.greenrobot.event.EventBus;

import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_DOOR_LOCK;
import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_DOOR_UNLOCK;
import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_FLASHLIGHTS;
import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_HORN;

/**
 * Created by maryan.melnychuk on 05.03.2015.
 */
public class DriveActivity extends MainActivity implements View.OnClickListener {

    private boolean mIsLocked = true;
    private ImageView mCenterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initFields();
                setUpUI();
                setWidgetsPosition(stub.getMeasuredWidth());
                setCenterImageSize();
                playStartAnimation();
            }
        });
    }

    protected void setUpUI() {
        mCircles[0].setCircleColor(getResources().getColor(R.color.category_drive));
        mRipples[0].setImageResource(R.drawable.fake_ripple_drive);
        mImages[0].setImageResource(R.mipmap.action_honk);

        mCircles[1].setCircleColor(getResources().getColor(R.color.category_drive));
        mRipples[1].setImageResource(R.drawable.fake_ripple_drive);
        mImages[1].setImageResource(R.mipmap.action_lights);

        mCircles[2].setCircleColor(getResources().getColor(R.color.category_drive));
        mRipples[2].setImageResource(R.drawable.fake_ripple_drive);
        mImages[2].setImageResource(R.mipmap.action_lock);

        mCenterImage = (ImageView) findViewById(R.id.center_image);
        mCenterImage.setImageResource(R.mipmap.drive_logo);
    }

    private void setCenterImageSize(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mCenterImage.getLayoutParams();
        lp.width = (int) (mImageSideSize * 0.8f);
        lp.height = (int) (mImageSideSize * 0.8f);
        mCenterImage.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.segment_top:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_FLASHLIGHTS));
                AnimationUtils.performActionAnimation(mImages[1], mImageSideSize);
                break;
            case R.id.segment_left:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(mIsLocked ? WEAR_ACTION_DOOR_UNLOCK : WEAR_ACTION_DOOR_LOCK));
                AnimationUtils.performToggleAnimation(mImages[2], mImageSideSize,
                        mIsLocked ? R.mipmap.action_unlock : R.mipmap.action_lock);
                mIsLocked = !mIsLocked;
                break;
            case R.id.segment_right:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_HORN));
                AnimationUtils.performActionAnimation(mImages[0], mImageSideSize);
                break;
        }
    }


}
