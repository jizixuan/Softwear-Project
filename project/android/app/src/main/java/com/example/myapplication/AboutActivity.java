package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.drakeet.about.AbsAboutActivity;
import com.drakeet.about.Card;
import com.drakeet.about.Category;
import com.drakeet.about.Contributor;
import com.drakeet.about.License;

import java.util.List;

public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("About Page By SchoolBill");
        version.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull List<Object> items) {
        items.add(new Category("介绍与帮助"));
        items.add(new Card("校园账单APP是一款面向当代大学生或在业青年人的理财APP，通过详细记录自己每一笔开销收入的多少来时刻记录自己的财产得失，进而帮助他们搭建正确的消费观念和理财意识，更好的管理自己的财务，从而实现合理的开销收入情况。"));

        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.avatar_drakeet, "JiZixvan", "Developer & designer", "http://weibo.com/drak11t"));
        items.add(new Contributor(R.drawable.avatar_drakeet, "ZhangZiheng", "Developer", "https://drakeet.me"));
        items.add(new Contributor(R.drawable.avatar_drakeet, "LongRui", "Developer"));

        items.add(new Category("Open Source Licenses"));
        items.add(new License("SchoolBill", "Agnoy_team", License.APACHE_2, "https://github.com/jizixuan/Softwear-Project"));

    }
}
