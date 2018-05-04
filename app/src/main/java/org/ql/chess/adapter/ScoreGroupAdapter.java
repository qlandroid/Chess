package org.ql.chess.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.ql.chess.R;
import org.ql.chess.entity.IEditInput;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/4/17
 *
 * @author ql
 */

public class ScoreGroupAdapter extends BaseQuickAdapter<IScoreGroupEntity, BaseViewHolder> {
    private OnTextChangeListener onTextChangeListener;

    public OnTextChangeListener getOnTextChangeListener() {
        return onTextChangeListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    public ScoreGroupAdapter() {
        super(R.layout.item_score_group_edit);
    }

    @Override
    protected void convert(BaseViewHolder helper, IScoreGroupEntity item) {
        EditText view = helper.getView(R.id.item_score_group_edit_et);
        setTextWatcher(item, view);
        helper.setText(R.id.item_score_group_edit_tv, item.getRoleName());
    }

    private void setTextWatcher(IEditInput item, EditText et) {
        final IEditInput data = item;
        Object tag = et.getTag();
        if (tag != null) {
            et.removeTextChangedListener((TextWatcher) tag);
        }
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(data.getInput())) {
                    data.setInput(s.toString());
                    return;
                }

                if (data.getInput().equals(s.toString())) {
                    return;
                }

                data.setInput(s.toString());
                if (onTextChangeListener != null) {
                    onTextChangeListener.onTextChange();
                }
            }
        };

        et.setTag(watcher);
        et.addTextChangedListener(watcher);
    }

    public interface OnTextChangeListener {
        void onTextChange();
    }
}
