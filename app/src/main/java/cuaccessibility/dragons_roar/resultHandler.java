package cuaccessibility.dragons_roar;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ai.api.model.Metadata;

import static java.lang.Integer.valueOf;


public class resultHandler{
    /*
    This class contains methods that:
    1: Take in input from API.AI's JSON results (Handled in the constructor)
    2: Using that information, determine what methods to call from charactersheet.java (This is done via getResponse).
    3: Return that output to AIButton.java to be read via TTS and logged. (getResponse calls methods that return stuff to be read)
    */

    HashMap<String, JsonElement> params;
    Metadata metadata;

    //This is where the current character should be loaded/referenced. For now it makes a new one from the constructor.
    CharacterSheet currentCharacter = new CharacterSheet();



    public resultHandler(HashMap<String, JsonElement> paramsInput, Metadata metadatainput )
    {

        /*params is a HashMap and contains the params.
        Each method within this class should be able to access
        the needed parameters in "params" as needed by using params.get(NameOfParameter)
        */
        params = paramsInput;

        /*metadata contains the intent ID, which is used to determine which method to call.*/
        metadata = metadatainput;
    }
    //end of constructor for resultHandler.

    /*
    getResponse() is what will be called outside of
    this class (from voiceButton) and is what will return the full string.
    This is used because something needs to determine which method to use.
     */
    public String getResponse(){
        if(metadata != null)
        {
            String command = metadata.getIntentName();

            //This switch statement is what determines the method to call and calls it.
            switch(command){

                case "Roll Dice":
                    return rollDice();

                case "Get Character Race":
                    return characterRace();

                //Other cases go here based on intent.

            }
        }
        return "Not a valid command";
    }//end getResponse


    /*This section should begin defining methods that:
    1: Access the information in params and metadata.
    2: Access the data in currentCharacter, passed in by reference from voiceButton.
    3: Generate a full response string that will be sent back to AIButton, which will read it off to the user.
    */
    public String rollDice() {

        //These two lines access params to get the die type and the number of dice to roll.
        int numOfDice = Integer.valueOf((params.get("number-integer").toString()).replace("\"", ""));
        int numOfSides = Integer.valueOf((params.get("Dice").toString()).replace("\"", ""));

        //These three lines are used for the random number generation section.
        Random r = new Random();
        int diceRollTotal = 0;
        int numOfDiceUse = numOfDice;
        //int individualResults[] = new int[numOfDice];

        /*
        Roll the die (numOfDice) times, and each time
        add it to the total for usage, and also
        add it to an array (individualResults) for later accessing if requested.
         */
        int newValue = 0;
        while (numOfDiceUse > 0)
            {
                newValue = r.nextInt(valueOf(numOfSides) + 1);
                diceRollTotal += newValue;
                //individualResults[(numOfDice - numOfDiceUse)] = newValue;
                numOfDiceUse = numOfDiceUse - 1;
            }

            StringBuilder diceRollStringBuild = new StringBuilder("");
            diceRollStringBuild.append(Integer.toString(numOfDice));
            diceRollStringBuild.append("d");
            diceRollStringBuild.append(Integer.toString(numOfSides));
            diceRollStringBuild.append(" = ");
            diceRollStringBuild.append(Integer.toString(diceRollTotal));
            String str = diceRollStringBuild.toString();
            return str;


    }//ends rollDice

    public String characterRace(){
        String characterRace = currentCharacter.getRace();
        return "You are a " + characterRace;
    }

}