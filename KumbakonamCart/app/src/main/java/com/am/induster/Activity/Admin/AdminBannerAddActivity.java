package com.am.induster.Activity.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.am.induster.R;
import com.am.induster.SupportingFiles.Util;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AdminBannerAddActivity extends AppCompatActivity
{
    private String CategoryName, subCategoryName, Description, Price, Pname, Poffer, POfferPrize, saveCurrentDate, saveCurrentTime, productType, POfferBrand;
    private Button AddNewProductButton;
    private Spinner categorySpinner;
    private Spinner subCategorySpinner;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, InputProductOffer, InputProductOfferPrize, InputProductBrandName;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl = "https://media.istockphoto.com/photos/various-car-parts-and-accessories-on-black-background-picture-id1150507732?k=6&m=1150507732&s=612x612&w=0&h=r7sxCeefmnPOpUJDuWGCPZDd23Vq1mUcjaIlp7ktuKo=";
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private static final int PICK_FROM_GALLERY = 1;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    ArrayList urlStrings;
    private TextView closeTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);


        CategoryName = "Royal Enfield";
        subCategoryName = "Royal Enfield All";
        productType = "Banner";
        ProductImagesRef = FirebaseStorage.getInstance().getReference("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference("Products");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage   = (ImageView) findViewById(R.id.select_product_image);
        InputProductName    = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_Description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        InputProductOffer = (EditText) findViewById(R.id.product_price_offer);
        InputProductOfferPrize = (EditText) findViewById(R.id.product_offer_prize);
        InputProductBrandName = (EditText) findViewById(R.id.product_brand_name);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        subCategorySpinner = (Spinner) findViewById(R.id.subcategory_spinner);
        closeTextBtn = (TextView) findViewById(R.id.close_add_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        loadingBar = new ProgressDialog(this);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.bike_category,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter
                .createFromResource(this, R.array.bike_sub_category,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(staticAdapter);
        subCategorySpinner.setAdapter(staticAdapter2);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                CategoryName = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                subCategoryName = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }



    private void OpenGallery()
    {
        try {
            if (ActivityCompat.checkSelfPermission(AdminBannerAddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdminBannerAddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GalleryPick);
            } else {
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
                startActivityForResult(galleryIntent, GalleryPick);*/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GalleryPick);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageList.clear();

            if (data.getClipData() != null) {

                int countClipData = data.getClipData().getItemCount();
                int currentImageSlect = 0;

                while (currentImageSlect < countClipData) {

                    ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                    ImageList.add(ImageUri);
                    currentImageSlect = currentImageSlect + 1;
                }

                InputProductImage.setImageURI(ImageUri);

                Toast.makeText(this, "You have selected" + ImageList.size() + "Images", Toast.LENGTH_SHORT).show();


            } else {
                ImageUri = data.getData();
                ImageList.add(ImageUri);
                InputProductImage.setImageURI(ImageUri);
            }
        }
    }


    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        Poffer = InputProductOffer.getText().toString();
        POfferPrize = InputProductOfferPrize.getText().toString();
        POfferBrand = InputProductBrandName.getText().toString();

        if (ImageList.size() <= 0)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Poffer))
        {
            Toast.makeText(this, "Please write product offer...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(POfferPrize))
        {
            Toast.makeText(this, "Please write product offer size...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadMultipleImage();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = Util.random();

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminBannerAddActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminBannerAddActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminBannerAddActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GalleryPick);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(AdminBannerAddActivity.this, "Allow the app to access gallery", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("productType", productType);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("poffer", Poffer);
        productMap.put("pofferprize", POfferPrize);
        productMap.put("productType", productType);
        productMap.put("pofferbrand", POfferBrand);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AdminBannerAddActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminBannerAddActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadMultipleImage() {
        urlStrings = new ArrayList<>();

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = Util.random();

        Toast.makeText(AdminBannerAddActivity.this, "If Loading Takes to long press button again", Toast.LENGTH_SHORT).show();
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

        for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {

            Uri IndividualImage = (Uri) ImageList.get(upload_count);
            final StorageReference ImageName = ProductImagesRef.child("Images" + IndividualImage.getLastPathSegment() + productRandomKey + ".jpg");

            ImageName.putFile(IndividualImage).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            urlStrings.add(String.valueOf(uri));

                                            if (urlStrings.size() == ImageList.size()){
                                                SaveProductInfoToDatabase(urlStrings);
                                            }

                                        }
                                    }
                            );
                        }
                    }
            );
        }

    }

    private void SaveProductInfoToDatabase(ArrayList<String> urlStrings) {

        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("ImgLink"+i, urlStrings.get(i));
        }

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", urlStrings.get(0));
        productMap.put("images", hashMap);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("poffer", Poffer);
        productMap.put("pofferprize", POfferPrize);
        productMap.put("productType", productType);
        productMap.put("pofferbrand", POfferBrand);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AdminBannerAddActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminBannerAddActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ImageList.clear();
    }
}

