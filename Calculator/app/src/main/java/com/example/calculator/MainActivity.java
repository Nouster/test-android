package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Ici je déclare les vues pour afficher les résultats et les solutions
    TextView result_View, solution_View;

    // et là, je déclare mes boutons
    MaterialButton buttonAC, buttonC, buttonMod;
    MaterialButton buttonDiv, buttonX, buttonMinus, buttonPlus, buttonEqual;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Je récupére le fichier de layout pour définir l'interface utilisateur

        // ici, j'initialise mes vues pour afficher les résultats
        result_View = findViewById(R.id.result_view);
        solution_View = findViewById(R.id.solution_view);

        // J'assigne des boutons à leurs IDs définis dans le fichier XML et j'ajout les écouteurs d'événements
        assignID(buttonAC, R.id.button_ac); // Bouton "Tout Effacer"
        assignID(buttonC, R.id.button_c);  // Bouton "Effacer le dernier caractère"
        assignID(buttonMod, R.id.button_mod); // Bouton "Modulo"
        assignID(buttonDiv, R.id.button_div);
        assignID(buttonX, R.id.button_X);
        assignID(buttonMinus, R.id.button_minus);
        assignID(buttonPlus, R.id.button_plus);
        assignID(buttonEqual, R.id.button_equal);
        assignID(button0, R.id.button_0);
        assignID(button1, R.id.button_1);
        assignID(button2, R.id.button_2);
        assignID(button3, R.id.button_3);
        assignID(button4, R.id.button_4);
        assignID(button5, R.id.button_5);
        assignID(button6, R.id.button_6);
        assignID(button7, R.id.button_7);
        assignID(button8, R.id.button_8);
        assignID(button9, R.id.button_9);
        assignID(buttonDot, R.id.button_dot); // Bouton "Point décimal"
    }

    // Méthode utilitaire pour associer un bouton à un ID et ajouter un écouteur d'événements
    void assignID(MaterialButton btn, int id) {
        btn = findViewById(id); // Trouve le bouton à partir de l'ID
        btn.setOnClickListener(this); // Ajoute un écouteur d'événements pour détecter les clics
    }

    // Méthode appelée lorsqu'un bouton est cliqué
    @Override
    public void onClick(View view) {
        // Récupération du bouton cliqué
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString(); // Texte du bouton cliqué
        String dataToCalculate = solution_View.getText().toString(); // Contenu actuel de l'entrée

        // Ici je gére les cas particuliers pour les boutons spécifiques
        if (buttonText.equals("AC")) { // Si le bouton "Tout Effacer" est cliqué
            solution_View.setText(""); // Réinitialise l'entrée
            result_View.setText("0"); // Réinitialise le résultat
            return;
        }

        if (buttonText.equals("=")) { // Si le bouton "Égal" est cliqué
            solution_View.setText(result_View.getText()); // Affiche le résultat final dans la zone d'entrée
            return;
        }

        if (buttonText.equals("C")) { // Si le bouton "Effacer le dernier caractère" est cliqué
            if (dataToCalculate.length() > 0) { // Vérifie qu'il y a quelque chose à supprimer
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1); // Supprime le dernier caractère
            }
        } else {
            dataToCalculate = dataToCalculate + buttonText; // Ajoute le texte du bouton à l'entrée
        }

        // Met à jour l'affichage de l'entrée
        solution_View.setText(dataToCalculate);

        // Calcule le résultat et l'affiche
        String finalResult = getResult(dataToCalculate);
        if (!finalResult.equals("Error")) { // Affiche uniquement les résultats valides
            result_View.setText(finalResult);
        }
    }

    // Méthode pour évaluer une expression mathématique en utilisant Rhino (moteur JavaScript)
    String getResult(String data) {
        try {
            Context context = Context.enter(); // Initialise le moteur Rhino
            context.setOptimizationLevel(-1); // Désactive les optimisations pour améliorer la compatibilité
            Scriptable scriptable = context.initStandardObjects(); // Initialise un environnement d'exécution standard
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString(); // Évalue l'expression
            return finalResult;
        } catch (Exception e) {
            return "Error"; // Retourne une erreur si l'évaluation échoue
        } finally {
            Context.exit(); // Ferme correctement le contexte Rhino, ça me permet d'éviter des fuites mémoires
        }
    }
}
