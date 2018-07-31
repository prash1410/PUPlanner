package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.Manifest;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateLocalProfile extends AppCompatActivity implements View.OnClickListener
{
    Button ChangeImage,Next;
    ImageView LocalProfileImage;
    final int PICK_FROM_CAMERA = 1;
    final int CROP_FROM_CAMERA = 2;
    final int PICK_FROM_GALLERY = 3;
    private Uri mImageCaptureUri;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    Context context;
    boolean UsingDefaultProfilePhoto = true;

    final String[] InputData = {"","",""};
    final String[] Departments = new String[]{"Select your department", "UIET", "UICET"};
    final String[] BranchesUIET = new String[]{"Select your branch", "CSE", "IT", "Biotech", "Mechanical", "Electrical"};
    final String[] BranchesUICET = new String[]{"Select your branch", "Chemical", "FT"};
    final String[] BranchesDefault = new String[]{"Select your department first"};
    final String[] Semesters = new String[]{"Select your semester","1st","2nd","3rd","4th","5th","6th","7th","8th"};

    EditText FirstName;
    EditText MiddleName;
    EditText LastName;
    EditText RollNo;
    EditText Email;

    TextView FirstNameError;
    TextView MiddleNameError;
    TextView LastNameError;
    TextView RollNoError;
    TextView EmailError;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create a local profile");
        setContentView(R.layout.create_local_profile);

        context = this;

        final Spinner spinner = findViewById(R.id.DepartmentSpinner);
        final Spinner spinner2 = findViewById(R.id.BranchSpinner);
        final Spinner semspinner = findViewById(R.id.SemSpinner);

        final TextView Deptt = findViewById(R.id.deptt);
        final TextView Bran = findViewById(R.id.bran);
        final TextView Sem = findViewById(R.id.sem);
        Next = findViewById(R.id.nextbutton);

        final TextView FirstNameTextView = findViewById(R.id.FirstNameTextView);
        final TextView MiddleNameTextView = findViewById(R.id.MiddleNameTextView);
        final TextView LastNameTextView = findViewById(R.id.LastNameTextView);
        final TextView RollNoTextView = findViewById(R.id.RollNoTextView);
        final TextView EmailTextView = findViewById(R.id.EmailTextView);

        FirstNameError = findViewById(R.id.FirstNameError);
        MiddleNameError = findViewById(R.id.MiddleNameError);
        LastNameError = findViewById(R.id.LastNameError);
        RollNoError = findViewById(R.id.RollNoError);
        EmailError = findViewById(R.id.EmailError);

        FirstName = findViewById(R.id.FirstName);
        MiddleName = findViewById(R.id.MiddleName);
        LastName = findViewById(R.id.LastName);
        RollNo = findViewById(R.id.RollNo);
        Email = findViewById(R.id.Email);

        ChangeImage = findViewById(R.id.pickimage);
        ChangeImage.setOnClickListener(this);
        Next.setOnClickListener(this);
        FirstName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    FirstNameError.setVisibility(View.INVISIBLE);
                }
                if(!hasFocus)
                {
                    if(!(FirstName.getText().toString()).isEmpty())
                    {
                        if(PerformValidation(0, FirstName.getText().toString()))
                        {
                            FirstNameError.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            FirstNameError.setText("Please remove any illegal characters");
                            FirstNameError.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        FirstNameError.setText("This field can't be empty");
                        FirstNameError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        MiddleName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    MiddleNameError.setVisibility(View.INVISIBLE);
                }
                if(!hasFocus)
                {
                    if(!(MiddleName.getText().toString()).isEmpty())
                    {
                        if(PerformValidation(0, MiddleName.getText().toString()))
                        {
                            MiddleNameError.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            MiddleNameError.setText("Please remove any illegal characters");
                            MiddleNameError.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        LastName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    LastNameError.setVisibility(View.INVISIBLE);
                }
                if(!hasFocus)
                {
                    if(!(LastName.getText().toString()).isEmpty())
                    {
                        if(PerformValidation(0, LastName.getText().toString()))
                        {
                            LastNameError.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            LastNameError.setText("Please remove any illegal characters");
                            LastNameError.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        LastNameError.setText("This field can't be empty");
                        LastNameError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        RollNo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    RollNoError.setVisibility(View.INVISIBLE);
                }
                if(!hasFocus)
                {
                    if(!(RollNo.getText().toString()).isEmpty())
                    {
                        if(PerformValidation(1, RollNo.getText().toString()))
                        {
                            RollNoError.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            RollNoError.setText("Please remove any illegal characters");
                            RollNoError.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        RollNoError.setText("This field can't be empty");
                        RollNoError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    EmailError.setVisibility(View.INVISIBLE);
                }
                if(!hasFocus)
                {
                    if(!(Email.getText().toString()).isEmpty())
                    {
                        if((Email.getText().toString()).contains(" "))
                        {
                            EmailError.setText("Please remove any spaces");
                            EmailError.setVisibility(View.VISIBLE);
                        }
                        if(!(Email.getText().toString()).contains("@"))
                        {
                            EmailError.setText("Email appears to be invalid");
                            EmailError.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        EmailError.setText("This field can't be empty");
                        EmailError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        final ScrollView scrollView = findViewById(R.id.LocalProfileScrollView);
         relativeLayout = findViewById(R.id.FocusThief);

        Email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    Email.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    relativeLayout.requestFocus();
                }
                return false;
            }
        });

        final List<String> BranchList = new ArrayList<>(Arrays.asList(BranchesDefault));
        final ArrayAdapter<String> spinner2ArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,BranchList)
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
        spinner2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinner2ArrayAdapter);
        spinner2.setVisibility(View.INVISIBLE);

        final List<String> BranchListUIET = new ArrayList<>(Arrays.asList(BranchesUIET));
        final ArrayAdapter<String> spinner2UArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,BranchListUIET)
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
        spinner2UArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final List<String> BranchListUICET = new ArrayList<>(Arrays.asList(BranchesUICET));
        final ArrayAdapter<String> spinner2CArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,BranchListUICET)
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
        spinner2CArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final List<String> DepList = new ArrayList<>(Arrays.asList(Departments));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DepList)
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
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position == 1)
                {
                    InputData[0] = "";
                    InputData[0] = selectedItemText;
                    spinner2.setAdapter(spinner2UArrayAdapter);
                    spinner2.setVisibility(View.VISIBLE);
                    Deptt.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
                if(position == 2)
                {
                    InputData[0] = "";
                    InputData[0] = selectedItemText;
                    spinner2.setAdapter(spinner2CArrayAdapter);
                    spinner2.setVisibility(View.VISIBLE);
                    Deptt.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        semspinner.setVisibility(View.INVISIBLE);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0)
                {
                    InputData[1] = "";
                    InputData[1] = selectedItemText;
                    //spinner2.setAdapter(spinner2UArrayAdapter);
                    semspinner.setVisibility(View.VISIBLE);
                    Bran.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        final List<String> SemList = new ArrayList<>(Arrays.asList(Semesters));
        final ArrayAdapter<String> SemArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,SemList)
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
        SemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semspinner.setAdapter(SemArrayAdapter);
        semspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0)
                {
                    InputData[2] = "";
                    InputData[2] = selectedItemText;
                    Sem.setVisibility(View.VISIBLE);
                    FirstName.setVisibility(View.VISIBLE);
                    FirstNameTextView.setVisibility(View.VISIBLE);
                    MiddleName.setVisibility(View.VISIBLE);
                    MiddleNameTextView.setVisibility(View.VISIBLE);
                    LastName.setVisibility(View.VISIBLE);
                    LastNameTextView.setVisibility(View.VISIBLE);
                    RollNo.setVisibility(View.VISIBLE);
                    RollNoTextView.setVisibility(View.VISIBLE);
                    Email.setVisibility(View.VISIBLE);
                    EmailTextView.setVisibility(View.VISIBLE);
                    Next.setVisibility(View.VISIBLE);
                    scrollView.smoothScrollTo(0,scrollView.getBottom());
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(final View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.pickimage:
                if(ContextCompat.checkSelfPermission(CreateLocalProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateLocalProfile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Change profile image");
                String[] choice = {"Click a new picture", "Select an existing picture"};
                if(!UsingDefaultProfilePhoto)
                {
                    choice = new String[]{"Click a new picture", "Select an existing picture","Reset to default picture"};
                }
                builder.setItems(choice, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), ".TmpImage" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                try
                                {
                                    intent.putExtra("return-data", true);
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (ActivityNotFoundException e)
                                {
                                    //
                                }

                                break;
                            case 1:
                                Intent GalleryIntent = new Intent();
                                GalleryIntent.setType("image/*");
                                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                GalleryIntent.putExtra("crop", "true");
                                GalleryIntent.putExtra("outputX", 300);
                                GalleryIntent.putExtra("outputY", 300);
                                //GalleryIntent.putExtra("aspectX", 0);
                                //GalleryIntent.putExtra("aspectY", 0);
                                try
                                {
                                    GalleryIntent.putExtra("return-data", true);
                                    startActivityForResult(Intent.createChooser(GalleryIntent,"Complete action using"), PICK_FROM_GALLERY);
                                } catch (ActivityNotFoundException ignored) {}
                                break;
                            case 2:
                                LocalProfileImage = findViewById(R.id.user_image_local);
                                LocalProfileImage.setImageResource(R.drawable.user);
                                UsingDefaultProfilePhoto = true;
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog = builder.create();
                dialog.show();
                if(ContextCompat.checkSelfPermission(CreateLocalProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    dialog.dismiss();
                }
                break;
            case R.id.nextbutton:
                if(ContextCompat.checkSelfPermission(CreateLocalProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateLocalProfile.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                    return;
                }
                relativeLayout.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(FinalValidation())
                {
                    AlertDialog.Builder Confirmation = new AlertDialog.Builder(this);
                    LayoutInflater inflater = this.getLayoutInflater();
                    @SuppressLint("InflateParams")
                    View dialogView = inflater.inflate(R.layout.dialog_confirmation,null);
                    Confirmation.setView(dialogView)
                            .setTitle("Proceed with the following profile?")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String profile = "Local";
                                    String Dep = InputData[0];
                                    String Bra = InputData[1];
                                    String Sem = InputData[2];
                                    String Fn = FirstName.getText().toString();
                                    String Mn = MiddleName.getText().toString();
                                    String Ln = LastName.getText().toString();
                                    String N;
                                    if(Mn.isEmpty())
                                    {
                                        N = Fn + " " + Ln;
                                    }
                                    else
                                    {
                                        N = Fn + " " +Mn+" "+ Ln;
                                    }
                                    String Roll = RollNo.getText().toString();
                                    String Email = RollNo.getText().toString();
                                    String Url = "";
                                    Database databaseobject = new Database(context);
                                    databaseobject.CreateProfile(profile,Dep,Bra,Sem,N,Roll,Email,Url);
                                    Intent i = new Intent(CreateLocalProfile.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog Conf = Confirmation.create();
                    String path = Environment.getExternalStorageDirectory().toString();
                    path += "/Android/data/com.puchd.puplanner/";
                    if(UsingDefaultProfilePhoto)
                    {
                        Bitmap DefaultPhoto = BitmapFactory.decodeResource(getResources(),R.drawable.user);
                        File DataDirectory = new File(path);
                        if(!DataDirectory.exists()) DataDirectory.mkdirs();
                        OutputStream fOut = null;
                        File LocalProfilePic = new File(path,"LocalProfilePic.png");
                        try
                        {
                            fOut = new FileOutputStream(LocalProfilePic);
                        } catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        DefaultPhoto.compress(Bitmap.CompressFormat.PNG,100,fOut);
                        try
                        {
                            Objects.requireNonNull(fOut).close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    File imgFile = new  File(path+"LocalProfilePic.png");
                    ImageView ImageConf = dialogView.findViewById(R.id.user_image_local_conf);
                    if(imgFile.exists())
                    {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ImageConf.setImageBitmap(myBitmap);
                    }
                    TextView DepttConf = dialogView.findViewById(R.id.depttconf);
                    DepttConf.setText("Department: "+InputData[0]);
                    TextView BranchConf = dialogView.findViewById(R.id.branconf);
                    BranchConf.setText("Branch: "+InputData[1]);
                    TextView SemConf = dialogView.findViewById(R.id.semconf);
                    SemConf.setText("Semester: "+InputData[2]);
                    TextView NameConf = dialogView.findViewById(R.id.nameconf);
                    if(!(MiddleName.getText().toString()).isEmpty())
                    {
                        NameConf.setText("Name: "+FirstName.getText().toString()+" "+MiddleName.getText().toString()+" "+LastName.getText().toString());
                    }
                    else
                    {
                        NameConf.setText("Name: "+FirstName.getText().toString()+" "+LastName.getText().toString());
                    }
                    Conf.show();
                    TextView RollNoConf = dialogView.findViewById(R.id.rollnoconf);
                    RollNoConf.setText("Roll No: "+RollNo.getText().toString());
                    TextView EmailConf = dialogView.findViewById(R.id.emailconf);
                    EmailConf.setText("Email: "+Email.getText().toString());
                    //Toast.makeText(getApplicationContext(),"Good to go!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    dialog.show();
                } else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Please grant the required permission", Snackbar.LENGTH_LONG).setAction("Grant",
                            new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    ActivityCompat.requestPermissions(CreateLocalProfile.this,
                                            new String[]{Manifest.permission
                                                    .WRITE_EXTERNAL_STORAGE},
                                            1);
                                }
                            }).show();
                }
                return;
            }
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Next.performClick();
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Please grant the required permission", Snackbar.LENGTH_LONG).setAction("Grant",
                            new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    ActivityCompat.requestPermissions(CreateLocalProfile.this,
                                            new String[]{Manifest.permission
                                                    .WRITE_EXTERNAL_STORAGE},
                                            1);
                                }
                            }).show();
                }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                final Bundle extras = data.getExtras();
                if (extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    LocalProfileImage = findViewById(R.id.user_image_local);
                    LocalProfileImage.setImageBitmap(getclip(Objects.requireNonNull(photo)));
                    String path = Environment.getExternalStorageDirectory().toString();
                    path += "/Android/data/com.puchd.puplanner/";
                    File DataDirectory = new File(path);
                    if(!DataDirectory.exists())
                    {
                        DataDirectory.mkdirs();
                    }
                    OutputStream fOut = null;
                    File LocalProfilePic = new File(path,"LocalProfilePic.png");
                    try
                    {
                        fOut = new FileOutputStream(LocalProfilePic);
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    Bitmap temp = getclip(photo);
                    temp.compress(Bitmap.CompressFormat.PNG,100,fOut);
                    try
                    {
                        Objects.requireNonNull(fOut).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UsingDefaultProfilePhoto = false;
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) f.delete();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(mgr).showSoftInput(LocalProfileImage, InputMethodManager.SHOW_IMPLICIT);
                break;
            }

            case PICK_FROM_GALLERY:
            {
                Bundle extras2 = data.getExtras();
                if (extras2 != null)
                {
                    Bitmap photo = extras2.getParcelable("data");
                    LocalProfileImage = findViewById(R.id.user_image_local);
                    LocalProfileImage.setImageBitmap(getclip(Objects.requireNonNull(photo)));
                    String path = Environment.getExternalStorageDirectory().toString();
                    path += "/Android/data/com.puchd.puplanner/";
                    File DataDirectory = new File(path);
                    if(!DataDirectory.exists())
                    {
                        DataDirectory.mkdirs();
                    }
                    OutputStream fOut = null;
                    File LocalProfilePic = new File(path,"LocalProfilePic.png");
                    try
                    {
                        fOut = new FileOutputStream(LocalProfilePic);
                    } catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    Bitmap temp = getclip(photo);
                    temp.compress(Bitmap.CompressFormat.PNG,100,fOut);
                    try
                    {
                        Objects.requireNonNull(fOut).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UsingDefaultProfilePhoto = false;
                }
                break;
            }

            case PICK_FROM_CAMERA:
            {
                try
                {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageCaptureUri,"image/*");
                    intent.putExtra("crop","true");
                    intent.putExtra("outputX", 300);
                    intent.putExtra("outputY", 300);
                    //intent.putExtra("aspectX", 1);
                    //intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_CAMERA);
                }
                catch (ActivityNotFoundException ignored){}
                break;
            }
        }
    }
    public static Bitmap getclip(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public boolean PerformValidation(int caller, String Data)
    {
        if(caller == 0)
        {
            char[] chars = Data.toCharArray();
            for (char c : chars)
            {
                if(!Character.isLetter(c))
                {
                    return false;
                }
            }
        }

        if(caller == 1)
        {
            char[] chars = Data.toCharArray();
            for (char c : chars)
            {
                if(!Character.isLetter(c))
                {
                    if(!Character.isDigit(c))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    public boolean FinalValidation()
    {
        int check = 0;
        if((FirstName.getText().toString()).isEmpty())
        {
            FirstNameError.setText("This field can't be empty");
            FirstNameError.setVisibility(View.VISIBLE);
            check++;
        }
        if((LastName.getText().toString()).isEmpty())
        {
            LastNameError.setText("This field can't be empty");
            LastNameError.setVisibility(View.VISIBLE);
            check++;
        }
        if((RollNo.getText().toString()).isEmpty())
        {
            RollNoError.setText("This field can't be empty");
            RollNoError.setVisibility(View.VISIBLE);
            check++;
        }
        if((Email.getText().toString()).isEmpty())
        {
            EmailError.setText("This field can't be empty");
            EmailError.setVisibility(View.VISIBLE);
            check++;
        }

        if(!(FirstName.getText().toString()).isEmpty()){
        char[] chars = FirstName.getText().toString().toCharArray();
        for (char c : chars)
        {
            if(!Character.isLetter(c))
            {
                FirstNameError.setText("Please remove any illegal characters");
                FirstNameError.setVisibility(View.VISIBLE);
                check++;
                break;
            }
        }}
        if(!(LastName.getText().toString()).isEmpty()){
        char[] chars1 = LastName.getText().toString().toCharArray();
        for (char c : chars1)
        {
            if(!Character.isLetter(c))
            {
                LastNameError.setText("Please remove any illegal characters");
                LastNameError.setVisibility(View.VISIBLE);
                check++;
                break;
            }
        }}
        if(!(MiddleName.getText().toString()).isEmpty())
        {
            char[] chars2 = MiddleName.getText().toString().toCharArray();
            for (char c : chars2)
            {
                if(!Character.isLetter(c))
                {
                    MiddleNameError.setText("Please remove any illegal characters");
                    MiddleNameError.setVisibility(View.VISIBLE);
                    check++;
                    break;
                }
            }
        }
        if(!(RollNo.getText().toString()).isEmpty())
        {
            char[] chars2 = RollNo.getText().toString().toCharArray();
            for (char c : chars2)
            {
                if(!Character.isLetter(c))
                {
                    if(!Character.isDigit(c))
                    {
                        RollNoError.setText("Please remove any illegal characters");
                        RollNoError.setVisibility(View.VISIBLE);
                        check++;
                        break;
                    }
                }
            }
        }
        if(!(Email.getText().toString()).isEmpty())
        {
            if((Email.getText().toString()).contains(" "))
            {
                EmailError.setText("This email appears to be invalid");
                EmailError.setVisibility(View.VISIBLE);
                check++;
            }
            if(!(Email.getText().toString()).contains("@"))
            {
                EmailError.setText("This email appears to be invalid");
                EmailError.setVisibility(View.VISIBLE);
                check++;
            }
            if(!(Email.getText().toString()).contains("."))
            {
                EmailError.setText("This email appears to be invalid");
                EmailError.setVisibility(View.VISIBLE);
                check++;
            }
        }
        if(check>0)
        {
            return false;
        }
        FirstNameError.setVisibility(View.INVISIBLE);
        LastNameError.setVisibility(View.INVISIBLE);
        MiddleNameError.setVisibility(View.INVISIBLE);
        RollNoError.setVisibility(View.INVISIBLE);
        EmailError.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select a different sign in option?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", false);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}