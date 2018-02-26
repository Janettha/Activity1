package janettha.activity1.Act3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Activity3 extends FragmentActivity {
    /* GOOGLE - Slide Between Fragments with ViewPager */
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public static final String ARG_tx = "emocion";
    private TextToSpeech tts;

    List<Emocion> emociones = new ArrayList<Emocion>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Emociones em = new Emociones();
        emociones = em.Emociones(getBaseContext(),"f");

        try {
            /* Se pasan parámetros a Bundle */
            Bundle b = new Bundle();
            mPager = (ViewPager) findViewById(R.id.pager);

            //b.putString(ARG_tx, emociones.get(0).getName());

            // Instantiate a ViewPager and a PagerAdapter.
            //FragmentManager fragmentManager = getSupportFragmentManager();
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);


        } catch (ClassCastException e) {
            Log.e("Fragment Manager", "Can't get fragment manager");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity3, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainmenuActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                if(mPager.getCurrentItem() != 0)
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                if (mPager.getCurrentItem() != NUM_PAGES)
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @SuppressLint("WrongViewCast")
        @Override
        public Fragment getItem(int position) {
            return FragmentAct3.create(position, getBaseContext(), position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
