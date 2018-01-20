package pae.seguros.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import pae.seguros.R;


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(IntroFragment.newInstance(R.layout.intro_slide_1));
        addSlide(IntroFragment.newInstance(R.layout.intro_slide_2));
        addSlide(IntroFragment.newInstance(R.layout.intro_slide_3));

        setSkipText(getString(R.string.skip));
        setDoneText(getString(R.string.done));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        finish();
    }
}
