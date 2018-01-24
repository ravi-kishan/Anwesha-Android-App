package info.anwesha2k18.iitp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import info.anwesha2k18.iitp.R;

import static info.anwesha2k18.iitp.R.id.textView;

public class EventInfoActivity extends AppCompatActivity {

    public static final String EXTRA_HEADER = "Header",
            EXTRA_LONG_DESCRIPTION = "LongDescription",
            EXTRA_RULES = "Rules",
            EXTRA_VENUE = "Venue",
            EXTRA_TIME = "Time",
            EXTRA_IMAGE_ID = "ImageId",
            EXTRA_ORGANIZERS = "Organizers",
            EXTRA_CONTACTS = "Contacts",
            EXTRA_ID = "ID",
            EXTRA_CODE = "Code",
            EXTRA_DAY = "Day",
            EXTRA_SIZE = "Size",
            EXTRA_FEE = "Fee",
            EXTRA_DATE = "Date",
            EXTRA_REG_URL = "RegURL";

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tb_event_info);

        // Get the Intent that started this activity and extract the strings needed
        Intent intent = getIntent();
        final String header = intent.getStringExtra(EXTRA_HEADER);
        String text = intent.getStringExtra(EXTRA_LONG_DESCRIPTION);
        String rules = intent.getStringExtra(EXTRA_RULES);
        final String dateTime = intent.getStringExtra(EXTRA_DATE) + ", " + intent.getStringExtra(EXTRA_TIME);
        String venue = intent.getStringExtra(EXTRA_VENUE);
        String imageURL = intent.getStringExtra(EXTRA_IMAGE_ID);
        String regURL = intent.getStringExtra(EXTRA_REG_URL);

        if (!regURL.equals("To be updated soon.") && !regURL.equals("") && !regURL.equals("null")) {
            ((TextView) findViewById(R.id.event_reg_link)).setText(
                    Html.fromHtml("<a href=\"" + regURL + "\">Register here!!</a>")
            );
            ((TextView) findViewById(R.id.event_reg_link)).setMovementMethod(LinkMovementMethod.getInstance());
//            Log.e("muks", "RegURL: <a href=\"" + regURL + "\">Register here!!</a>");
        } else {
            ((TextView) findViewById(R.id.event_reg_link)).setText(getString(R.string.will_be_updated_soon));
//            Log.e("muks", "else here");
        }

        AppBarLayout appBarLayout = findViewById(R.id.appbar_event_info);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(header);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        ((TextView) findViewById(R.id.event_info_name)).setText(header);
        Glide.with(this)
                .load(imageURL)
                .apply(new RequestOptions().error(R.drawable.anwesha_placeholder))
                .into((ImageView) findViewById(R.id.event_info_imageview));

        if (text.equals("-1"))
            text = "No Information Available";
        final String finalText = text;
        ((TextView) findViewById(R.id.event_info_textview)).setText(text);

//        Log.e("muks", "rules = " + rules);
        if (!rules.equals("To be updated soon.") && !rules.equals("") && !rules.equals("null")) {
            TextView rulesTextView = findViewById(R.id.event_rules_textview);
            rulesTextView.setText(Html.fromHtml("<a href=\"" + rules + "\">Find the rules here.</a>"));
            Log.e("muks", "Rules: <a href=\"" + rules + "\">Find the rules here.</a>");
            rulesTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        String organizers = intent.getStringExtra(EXTRA_ORGANIZERS);
//        final String contacts = intent.getStringExtra(EXTRA_CONTACTS);
//        if (organizers.equals("-1"))
//            organizers = "No information available";
        ((TextView) findViewById(R.id.event_organizers)).setText(organizers);
//        ((TextView) findViewById(R.id.event_contact)).setText(contacts);

        ((TextView) findViewById(R.id.event_date_time)).setText(dateTime);
        ((TextView) findViewById(R.id.event_venue)).setText(venue);

        FloatingActionButton fab = findViewById(R.id.fab_share_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources resources = getResources();
                String shareString = resources.getText(R.string.share_message) + "\n"
                        + resources.getText(R.string.name) + ": " + header + "\n"
                        + resources.getText(R.string.date_time) + ": " + dateTime + "\n"
                        + finalText;
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share_to)));
            }
        });

        if (!header.equals("MR. AND MS. ANWESHA"))
            findViewById(R.id.event_reg_link).setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
