/*
 * Copyright 2014 Toxic Bakery
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.onionpie.pandorabox.Widget.ViewPagerAnimation;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class RotateDownTransformer extends ABaseTransformer {

	private static final float ROT_MOD = -90f;


	@Override
	protected void onTransform(View view, float position) {

		final float width = view.getMeasuredWidth();
//		final float height = view.getMeasuredHeight();
		View child = ((ViewGroup) view).getChildAt(0);
//		ViewPager parent = (ViewPager) view.getParent();
//		position -= parent.getPaddingRight() / width;
		final float rotation = ROT_MOD * position * -1.25f;

		Log.d("test", position + "  " + view.getWidth() + "  " + child.getMeasuredHeight());

		view.setPivotX(width * 0.5f);
		view.setPivotY(child.getMeasuredHeight());
//		view.setRotationX(width * 0.5f);
//		view.setRotationY(0);
		view.setRotation(rotation);
		if (position < 0)
			view.setAlpha(position + 1);
		else
			view.setAlpha(Math.abs(position - 1));
	}



	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
