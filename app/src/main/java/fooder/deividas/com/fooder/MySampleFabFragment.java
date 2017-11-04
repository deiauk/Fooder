package fooder.deividas.com.fooder;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import fooder.deividas.com.fooder.database.models.Category;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySampleFabFragment extends AAH_FabulousFragment {

    private ImageButton imgbtn_refresh, imgbtn_apply;
    private DisplayMetrics metrics;
    private Realm realm;

    private List<Integer> selectedList;

    public MySampleFabFragment() {
        // Required empty public constructor
    }


    public static MySampleFabFragment newInstance() {
        MySampleFabFragment f = new MySampleFabFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///applied_filters = ((MainActivity) getActivity()).getApplied_filters();
        metrics = this.getResources().getDisplayMetrics();
        selectedList = new ArrayList<>();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        realm = ((MyApplication) getActivity().getApplicationContext()).getRealm();
        final View contentView = View.inflate(getContext(), R.layout.fragment_my_sample_fab, null);
        RelativeLayout rl_content = contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = contentView.findViewById(R.id.ll_buttons);
        imgbtn_refresh = contentView.findViewById(R.id.imgbtn_refresh);
        imgbtn_apply = contentView.findViewById(R.id.imgbtn_apply);

        imgbtn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter(null);
            }
        });
        imgbtn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (TextView tv : textviews) {
//                    tv.setTag("unselected");
//                    tv.setBackgroundResource(R.drawable.chip_unselected);
//                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
//                }
                //applied_filters.clear();
            }
        });


        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        final TagFlowLayout filterContent = contentView.findViewById(R.id.filterContent);

        final List<Category> categories = realm.where(Category.class).findAll();
        filterContent.setAdapter(new TagAdapter<Category>(categories) {
            @Override
            public View getView(FlowLayout parent, int position, Category category) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, filterContent, false);
                tv.setText(category.getName());
                selectedList.add(category.getId());
                //tv.setBackgroundResource(R.drawable.checked_bg);
                return tv;
            }
        });

        setAnimationDuration(300); //optional; default 500ms
        setPeekHeight(290); // optional; default 400dp
        setCallbacks((Callbacks) getActivity()); //optional; to get back result
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

}
