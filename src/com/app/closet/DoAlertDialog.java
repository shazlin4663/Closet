package com.app.closet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DoAlertDialog {

	private AlertDialog.Builder	_builder;
	private Context				_context;
	private AlertDialog			_dialog;
	private String _entry;
	
	public String getEntry() {
		return _entry;
	}

	public void setEntry(String entry) {
		this._entry = entry;
	}

	public DoAlertDialog(Context context) {
		_context = context;
	}

	@SuppressLint("NewApi")
	public void createNeutralAlertDialog(String title, String message) {
		_builder = new AlertDialog.Builder(_context);

		_builder.setTitle(title).setMessage(message).setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		}).setCancelable(false);
		_dialog = _builder.create();

		_dialog.show();
	}

	public AlertDialog editEntryAlertDialog() {
		_builder = new AlertDialog.Builder(_context);
		LayoutInflater inflater = LayoutInflater.from(_context);
		View view = inflater.inflate(R.layout.alert_dialog_text_entry, null);
	
		final EditText etEdit = (EditText) view.findViewById(R.id.etDialogEntry);
	
		_builder.setView(view)
		.setView(view)
		.setPositiveButton("Update", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String temp = etEdit.getText().toString();
				_entry = temp;
			}
		}).setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				_entry = "";	
			}
		}).setCancelable(false);
		return _builder.create();
	}
}
