package janettha.activity1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ImageView mImageView;

    //TABLAYOUT
    List<Fragment> tabFragmentList = new ArrayList<Fragment>();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //Authentification Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //Storage
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private List<ImageUpload> imgList;

    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private ProgressDialog progressDialog;

    private Uri imgUri;

    public final static String FB_STORAGE_PATH = "sentimientos/";
    public final static String FB_DATABASE_PATH = "SENTIMIENTOS/SENTIMIENTOS_F";
    public final static int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment f;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //Firebase Authentification
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Firebase storage
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        mUsername = ANONYMOUS;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mImageView = (ImageView)findViewById(R.id.imgView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override   //capta modificaciones del servidor - siempre debe ir
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // user is signed in
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(user.getDisplayName());
                }else{
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
                    imgUri = data.getData();

                    try{
                        Bitmap bn = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                        mImageView.setImageBitmap(bn);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //Las dos lineas hacen que se destruya la actividad
        //detachDatabaseReadListener();
        //Hace que este realmente este limpio el listener
        //mMessageAdapter.clear();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
        */
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                //sign out
                FirebaseAuth.getInstance().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private int image;
        private int color;

        public PlaceholderFragment() {
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, FirebaseStorage mFirebaseStorage, Uri uri) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl("gs://adhdact1.appspot.com/sentimientos").child("PF-ABURRIMIENTO.png");
            StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/adhdact1.appspot.com/o/sentimientos%2FPF-ABURRIMIENTO.png?alt=media&token=e36b7f15-42ef-48c8-bb31-6f0223fb0965");
            //fragment.setArguments(args);

            int imgResId = 0;
            int colorResId = 0;

            if(sectionNumber==0){
                //     imgResId = fragment.getResources().getIdentifier("duck", "drawable","janettha.androidtablayout");
                //Get storage reference
                //StorageReference ref = storageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + ".png");
                //Glide.with(context).load(uri).into(mImageView);

                Glide.with(mImageView.getContext() /* context */).load(storageRef).into(mImageView);

                colorResId = R.color.color1;
                imgResId = R.id.imgView;
                args.putInt("image", imgResId);
                args.putInt("color", colorResId);
            } else if (sectionNumber==2){
                //imgResId = fragment.getResources().getIdentifier("parrot", "drawable","janettha.androidtablayout");
                imgResId = R.drawable.parrot;
                colorResId = R.color.color2;
                args.putInt("image", imgResId);
                args.putInt("color", colorResId);
            } else if(sectionNumber==3){
                //   imgResId = fragment.getResources().getIdentifier("cock", "drawable","janettha.androidtablayout");
                imgResId = R.drawable.cock;
                colorResId = R.color.color3;
                args.putInt("image", imgResId);
                args.putInt("color", colorResId);
            }

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) view.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            ImageView tabImage = (ImageView) view.findViewById(R.id.imgView);
            tabImage.setImageResource(image);
            view.setBackgroundResource(color);
            return view;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        image = bundle.getInt("image");
        color = bundle.getInt("color");
    }
}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1, mFirebaseStorage, imgUri);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
