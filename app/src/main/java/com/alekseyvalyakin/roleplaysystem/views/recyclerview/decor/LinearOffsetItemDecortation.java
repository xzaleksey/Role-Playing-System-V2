package com.alekseyvalyakin.roleplaysystem.views.recyclerview.decor;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * DividerItemDecoration is a {@link RecyclerView.ItemDecoration} that can be used as a divider
 * between items of a {@link LinearLayoutManager}. It supports both {@link #HORIZONTAL} and
 * {@link #VERTICAL} orientations.
 *
 * <pre>
 *     mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
 *             mLayoutManager.getOrientation());
 *     recyclerView.addItemDecoration(mDividerItemDecoration);
 * </pre>
 */
public class LinearOffsetItemDecortation extends RecyclerView.ItemDecoration {
  public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  public static final int VERTICAL = LinearLayout.VERTICAL;

  private int offset;

  /**
   * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
   */
  private int mOrientation;

  private final Rect mBounds = new Rect();

  /**
   * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
   * {@link LinearLayoutManager}.
   *
   * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
   */
  public LinearOffsetItemDecortation(int orientation, int offset) {
    this.offset = offset;
    setOrientation(orientation);
  }

  /**
   * Sets the orientation for this divider. This should be called if
   * {@link RecyclerView.LayoutManager} changes orientation.
   *
   * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
   */
  public void setOrientation(int orientation) {
    if (orientation != HORIZONTAL && orientation != VERTICAL) {
      throw new IllegalArgumentException(
          "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
    mOrientation = orientation;
  }

  /**
   * Sets the {@link Drawable} for this divider.
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    if (mOrientation == VERTICAL) {
      outRect.set(0, 0, 0, offset);
    } else {
      outRect.set(0, 0, offset, 0);
    }
  }
}

