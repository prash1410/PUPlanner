package com.puchd.puplanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginCheck extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{
    EditText GRollNo;
    TextView GRollError;
    String personPhotoUrl;
    String personName;
    String email;
    RelativeLayout relativeLayout;
    final String[] InputData = {"","",""};
    final String[] Departments = new String[]{"Select your department", "UIET", "UICET"};
    final String[] BranchesUIET = new String[]{"Select your branch", "CSE", "IT", "Biotech", "Mechanical", "Electrical"};
    final String[] BranchesUICET = new String[]{"Select your branch", "Chemical", "FT"};
    final String[] BranchesDefault = new String[]{"Select your department first"};
    final String[] Semesters = new String[]{"Select your semester","1st","2nd","3rd","4th","5th","6th","7th","8th"};

    private GoogleApiClient mGoogleApiClient;
    Button GSignInButton,GLogOutButton,LocalButton, Continue;
    TextView UserName,UserEmail;
    ImageView ProfilePic;
    ProgressDialog mProgressDialog,progressDialog;
    private static final int RC_SIGN_IN = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Choose a sign in method");
        setContentView(R.layout.login_check);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        UserName = findViewById(R.id.userName);
        UserEmail = findViewById(R.id.userEmail);
        ProfilePic = findViewById(R.id.user_image);
        GRollNo = findViewById(R.id.GRollNo);
        GRollError = findViewById(R.id.GRollNoError);
        GSignInButton = findViewById(R.id.GSignIn);
        GLogOutButton = findViewById(R.id.GLogOut);
        LocalButton = findViewById(R.id.LocalLogIn);
        Continue = findViewById(R.id.ContinueButton);
        relativeLayout = findViewById(R.id.GFocusThief);
        GSignInButton.setOnClickListener(this);
        GLogOutButton.setOnClickListener(this);
        LocalButton.setOnClickListener(this);
        Continue.setOnClickListener(this);
    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status)
                    {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            personName = Objects.requireNonNull(acct).getDisplayName();
            if(acct.getPhotoUrl() == null)
            {
                personPhotoUrl = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg";
            }
            else
            {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            email = acct.getEmail();
            UserName.setText(personName);
            UserName.setVisibility(View.VISIBLE);
            UserEmail.setText(email);
            UserEmail.setVisibility(View.VISIBLE);
            Glide.with(this).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ProfilePic);
            updateUI(true);
            hideProgressDialog();
        }
        else
        {
            hideProgressDialog();
            updateUI(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone())
        {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else
        {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showLoadingDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>()
            {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult)
                {
                    //hideLoadingDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void showLoadingDialog()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.credentials));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideLoadingDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.GSignIn:
                showLoadingDialog();
                if(isNetworkConnected())
                {
                    hideLoadingDialog();
                    showProgressDialog();
                    signIn();
                    break;
                }
                else
                {
                    hideLoadingDialog();
                    Snackbar.make(v, "No internet connection detected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    break;
                }

            case R.id.GLogOut:
                signOut();
                break;
            case R.id.LocalLogIn:
                Intent i = new Intent(this, CreateLocalProfile.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.ContinueButton:
                if(ContextCompat.checkSelfPermission(LoginCheck.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(LoginCheck.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
                if(mProgressDialog!=null)mProgressDialog.dismiss();
                ContinueAction();
                break;
            case R.id.Gnextbutton:
                relativeLayout.requestFocus();
                if(Validate())
                {
                    AlertDialog.Builder Gdialog = new AlertDialog.Builder(this);
                    Gdialog.setMessage("Proceed with this profile?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Database database = new Database(getApplicationContext());
                                    database.CreateProfile("Google",InputData[0],InputData[1],InputData[2],personName,GRollNo.getText().toString(),email,personPhotoUrl);
                                    Intent i = new Intent(LoginCheck.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = Gdialog.create();
                    dialog.show();
                }
                break;
        }
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn)
        {
            GSignInButton.setVisibility(View.GONE);
            GLogOutButton.setVisibility(View.VISIBLE);
            LocalButton.setVisibility(View.GONE);
            UserEmail.setVisibility(View.VISIBLE);
            UserName.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Continue with Google account");
            Continue.setVisibility(View.VISIBLE);
        }
        else
        {
            GSignInButton.setVisibility(View.VISIBLE);
            GLogOutButton.setVisibility(View.GONE);
            LocalButton.setVisibility(View.VISIBLE);
            ProfilePic.setImageResource(R.drawable.user);
            UserEmail.setText("");
            UserName.setText("");
            UserEmail.setVisibility(View.GONE);
            UserName.setVisibility(View.GONE);
            Continue.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Choose a sign in method");
        }
    }

    private void ContinueAction()
    {
        if(mProgressDialog!=null) mProgressDialog.dismiss();
        final Spinner Gspinner = findViewById(R.id.GDepartmentSpinner);
        final Spinner Gspinner2 = findViewById(R.id.GBranchSpinner);
        final Spinner Gsemspinner = findViewById(R.id.GSemSpinner);

        final TextView GDeptt = findViewById(R.id.Gdeptt);
        final TextView GBran = findViewById(R.id.Gbran);
        final TextView GSem = findViewById(R.id.Gsem);
        final TextView GRollTextView = findViewById(R.id.GRollNoTextView);

        final Button GNext = findViewById(R.id.Gnextbutton);
        GNext.setOnClickListener(this);

        Continue.setVisibility(View.GONE);
        GLogOutButton.setVisibility(View.GONE);
        Gspinner.setVisibility(View.VISIBLE);
        final List<String> GBranchList = new ArrayList<>(Arrays.asList(BranchesDefault));
        final ArrayAdapter<String> Gspinner2ArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,GBranchList)
        {
            @Override
            public boolean isEnabled(int position)
            {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){tv.setTextColor(Color.GRAY);}
                else {tv.setTextColor(Color.BLACK);}
                return view;
            }
        };
        Gspinner2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gspinner2.setAdapter(Gspinner2ArrayAdapter);
        Gspinner2.setVisibility(View.INVISIBLE);

        final List<String> GBranchListUIET = new ArrayList<>(Arrays.asList(BranchesUIET));
        final ArrayAdapter<String> Gspinner2UArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,GBranchListUIET)
        {
            @Override
            public boolean isEnabled(int position)
            {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){tv.setTextColor(Color.GRAY);}
                else {tv.setTextColor(Color.BLACK);}
                return view;
            }
        };
        Gspinner2UArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final List<String> GBranchListUICET = new ArrayList<>(Arrays.asList(BranchesUICET));
        final ArrayAdapter<String> Gspinner2CArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,GBranchListUICET)
        {
            @Override
            public boolean isEnabled(int position)
            {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){tv.setTextColor(Color.GRAY);}
                else {tv.setTextColor(Color.BLACK);}
                return view;
            }
        };
        Gspinner2CArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        final List<String> GDepList = new ArrayList<>(Arrays.asList(Departments));
        final ArrayAdapter<String> GspinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,GDepList)
        {
            @Override
            public boolean isEnabled(int position)
            {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){tv.setTextColor(Color.GRAY);}
                else {tv.setTextColor(Color.BLACK);}
                return view;
            }
        };
        GspinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gspinner.setAdapter(GspinnerArrayAdapter);

        Gspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position == 1)
                {
                    InputData[0] = "";
                    InputData[0] = selectedItemText;
                    Gspinner2.setAdapter(Gspinner2UArrayAdapter);
                    Gspinner2.setVisibility(View.VISIBLE);
                    GDeptt.setVisibility(View.VISIBLE);
                }
                if(position == 2)
                {
                    InputData[0] = "";
                    InputData[0] = selectedItemText;
                    Gspinner2.setAdapter(Gspinner2CArrayAdapter);
                    Gspinner2.setVisibility(View.VISIBLE);
                    GDeptt.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Gsemspinner.setVisibility(View.INVISIBLE);
        Gspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0)
                {
                    InputData[1] = "";
                    InputData[1] = selectedItemText;
                    Gsemspinner.setVisibility(View.VISIBLE);
                    GBran.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        final List<String> GSemList = new ArrayList<>(Arrays.asList(Semesters));
        final ArrayAdapter<String> GSemArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,GSemList)
        {
            @Override
            public boolean isEnabled(int position)
            {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){tv.setTextColor(Color.GRAY);}
                else {tv.setTextColor(Color.BLACK);}
                return view;
            }
        };
        final ScrollView scrollView = findViewById(R.id.GLocalProfileScrollView);
        GSemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gsemspinner.setAdapter(GSemArrayAdapter);
        Gsemspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0)
                {
                    InputData[2] = "";
                    InputData[2] = selectedItemText;
                    GSem.setVisibility(View.VISIBLE);
                    GRollNo.setVisibility(View.VISIBLE);
                    GRollTextView.setVisibility(View.VISIBLE);
                    GNext.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0,scrollView.getBottom());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        GRollNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    GRollNo.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    relativeLayout.requestFocus();
                }
                return false;
            }
        });
        GRollNo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    GRollError.setVisibility(View.GONE);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public boolean Validate()
    {
        if((GRollNo.getText().toString()).isEmpty())
        {
            GRollError.setText("This field can't be empty");
            GRollError.setVisibility(View.VISIBLE);
            return false;
        }
        if(!(GRollNo.getText().toString()).isEmpty())
        {
            char[] chars2 = GRollNo.getText().toString().toCharArray();
            for (char c : chars2)
            {
                if(!Character.isLetter(c))
                {
                    if(!Character.isDigit(c))
                    {
                        GRollError.setText("Please remove any illegal characters");
                        GRollError.setVisibility(View.VISIBLE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Continue.performClick();
                }
                else
                {
                    mProgressDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Please grant the required permission", Snackbar.LENGTH_LONG).setAction("Grant",
                            new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    ActivityCompat.requestPermissions(LoginCheck.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            }).show();
                }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
