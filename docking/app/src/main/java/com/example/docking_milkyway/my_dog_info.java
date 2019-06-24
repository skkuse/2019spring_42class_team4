package com.example.docking_milkyway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class my_dog_info extends AppCompatActivity {

    Context context = this;

    Button dogspeciesinsert, dogageinsert, dogregister;
    EditText dognameinsert;

    ArrayAdapter<String> dogspeciesadapter;
    ArrayAdapter<String> dogageadapter;

    String dogage, dogspecies;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dog_info);

        dogspeciesinsert = findViewById(R.id.dogspeciesinsert);
        dogageinsert = findViewById(R.id.dogageinsert);
        dogregister = findViewById(R.id.dogregister);
        dognameinsert = findViewById(R.id.dognameinsert);

        dogspeciesadapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        for(int index = 0 ; index < 25 ; index++) {
            dogspeciesadapter.add(Integer.toString(index));
        }
        dogspeciesadapter.notifyDataSetChanged();

        dogspeciesinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("성별 선택"); //타이틀

                alert.setAdapter(dogspeciesadapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dogspecies = dogspeciesadapter.getItem(which);
                    }
                });
                alert.show();
            }
        });

        dogageadapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        dogageadapter.addAll("Affenpinscher", "Afghan Hound", "Airedale Terrier", "Akita", "Alaskan Klee Kai", "Alaskan Malamute", "American Bulldog",
                "American English Coonhound", "American Eskimo Dog", "American Foxhound", "American Pit Bull Terrier", "American Staffordshire Terrier",
                "American Water Spaniel", "Anatolian Shepherd Dog", "Appenzeller Sennenhunde", "Australian Cattle Dog", "Australian Kelpie", "Australian Shepherd",
                "Australian Terrier", "Azawakh", "Barbet", "Basenji", "Basset Hound", "Beagle", "Bearded Collie", "Bedlington Terrier", "Belgian Malinois",
                "Belgian Sheepdog", "Belgian Tervuren", "Berger Picard", "Bernedoodle", "Bernese Mountain Dog", "Bichon Frise", "Black Mouth Cur", "Black Russian Terrier",
                "Black and Tan Coonhound", "Bloodhound", "Blue Lacy", "Bluetick Coonhound", "Boerboel", "Bolognese", "Border Collie", "Border Terrier", "Borzoi",
                "Boston Terrier", "Bouvier des Flandres", "Boxer", "Boykin Spaniel", "Bracco Italiano", "Briard", "Brittany", "Brussels Griffon", "Bull Terrier", "Bulldog",
                "Bullmastiff", "Cairn Terrier", "Canaan Dog", "Cane Corso", "Cardigan Welsh Corgi", "Catahoula Leopard Dog", "Caucasian Shepherd Dog",
                "Cavalier King Charles Spaniel", "Cesky Terrier", "Chesapeake Bay Retriever", "Chihuahua", "Chinese Crested", "Chinese Shar-Pei", "Chinook",
                "Chow Chow", "Clumber Spaniel", "Cockapoo", "Cocker Spaniel", "Collie", "Coton de Tulear", "Curly-Coated Retriever", "Dachshund", "Dalmatian",
                "Dandie Dinmont Terrier", "Doberman Pinscher", "Dogo Argentino", "Dogue de Bordeaux", "Dutch Shepherd", "English Cocker Spaniel", "English Foxhound",
                "English Setter", "English Springer Spaniel", "English Toy Spaniel", "Entlebucher Mountain Dog", "Field Spaniel", "Finnish Lapphund", "Finnish Spitz",
                "Flat-Coated Retriever", "Fox Terrier", "French Bulldog", "German Pinscher", "German Shepherd Dog", "German Shorthaired Pointer", "German Wirehaired Pointer",
                "Giant Schnauzer", "Glen of Imaal Terrier", "Goldador", "Golden Retriever", "Goldendoodle", "Gordon Setter", "Great Dane", "Great Pyrenees",
                "Greater Swiss Mountain Dog", "Greyhound", "Harrier", "Havanese", "Ibizan Hound", "Icelandic Sheepdog", "Irish Red and White Setter", "Irish Setter",
                "Irish Terrier", "Irish Water Spaniel", "Irish Wolfhound", "Italian Greyhound", "Jack Russell Terrier", "Japanese Chin", "Japanese Spitz",
                "Karelian Bear Dog", "Keeshond", "Kerry Blue Terrier", "Komondor", "Kooikerhondje", "Kuvasz", "Labradoodle", "Labrador Retriever",
                "Lagotto Romagnolo", "Lakeland Terrier", "Lancashire Heeler", "Leonberger", "Lhasa Apso", "Lowchen", "Maltes", "Maltese Shih Tzu",
                "Maltipoo", "Manchester Terrier", "Mastiff", "Miniature Pinscher", "Miniature Schnauzer", "Mudi", "Mutt", "Neapolitan Mastiff", "Newfoundland",
                "Norfolk Terrier", "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier", "Nova Scotia Duck Tolling Retriever",
                "Old English Sheepdog", "Otterhound", "Papillon", "Peekapoo", "Pekingese", "Pembroke Welsh Corgi", "Petit Basset Griffon Vendeen", "Pharaoh Hound",
                "Plott", "Pocket Beagle", "Pointer", "Polish Lowland Sheepdog", "Pomeranian", "Pomsky", "Poodle", "Portuguese Water Dog", "Pug", "Puggle", "Puli",
                "Pyrenean Shepherd", "Rat Terrier", "Redbone Coonhound", "Rhodesian Ridgeback", "Rottweiler", "Saint Bernard", "Saluki", "Samoyed", "Schipperke",
                "Schnoodle", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Shetland Sheepdog", "Shiba Inu", "Shih Tzu", "Siberian Husky",
                "Silken Windhound", "Silky Terrier", "Skye Terrier", "Sloughi", "Small Munsterlander Pointer", "Soft Coated Wheaten Terrier", "Stabyhoun",
                "Staffordshire Bull Terrier", "Standard Schnauzer", "Sussex Spaniel", "Swedish Vallhund", "Tibetan Mastiff", "Tibetan Spaniel", "Tibetan Terrier",
                "Toy Fox Terrier", "Treeing Tennessee Brindle", "Treeing Walker Coonhound", "Vizsla", "Weimaraner", "Welsh Springer Spaniel", "Welsh Terrier",
                "West Highland White Terrier", "Whippet", "Wirehaired Pointing Griffon", "Yorkipoo", "Yorkshire Terrier"
        );
        dogageadapter.notifyDataSetChanged();

        dogageinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("성별 선택"); //타이틀

                alert.setAdapter(dogageadapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dogage = dogageadapter.getItem(which);
                    }
                });
                alert.show();
            }
        });

    }
}
