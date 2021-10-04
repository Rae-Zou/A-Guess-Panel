package View;

import Clues.Card;
import Clues.Deck;
import Player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * A Guess Panel which allows the player to make a guess with displayed radio boxes.
 */
public class GuessPanel extends Observable {

    private Map<String, JRadioButton> characterSelButtons = new HashMap<>();

    private boolean guess = false;
    private ButtonGroup characterGroup;
    private ButtonGroup weaponGroup;
    private ButtonGroup roomGroup;
    private JButton guessButton;


    /**
     * set up the panel with all the options.
     * Character options
     * Weapon options
     * Player can press the make guess button to submit the guess
     * @return the set panel
     */
    public JPanel setGuessPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(300,300);
        buttonPanel.setLayout(new GridLayout(22,1));

        buttonPanel.add(new JLabel("Make a Selection: "));

        //set the characters radio button
        this.setCharacters(buttonPanel);

        //set the weapons radio button
        this.setWeapons(buttonPanel);

        if(!guess) {
            //set the rooms radio button
            this.setRooms(buttonPanel);
        }

        guessButton = new JButton("Submit");
        buttonPanel.add(guessButton);
        guessButton.addActionListener(ev -> {

            ArrayList<Card> guess = new ArrayList<>();

            for (JRadioButton j : characterSelButtons.values()) {

                if (j.getText().equals(getSelectedButton(characterGroup))) {
                    guess.add(new Card(Card.type.Player, j.getText()));
                } else if (j.getText().equals(getSelectedButton(weaponGroup))) {
                    guess.add(new Card(Card.type.Weapon, j.getText()));
                } else if (j.getText().equals(getSelectedButton(roomGroup))) {
                    guess.add(new Card(Card.type.Estate, j.getText()));
                }

            }

            // fire off to observer
            setChanged();
            notifyObservers(guess);

            setAllEnable(false);
        });

        buttonPanel.setBorder(new EmptyBorder(20,30,50,30));
        return buttonPanel;
    }



    /**
     * set up the Characters radio boxes
     * @param buttonPanel current panel
     */
    public void setCharacters(JPanel buttonPanel){

        buttonPanel.add(new JLabel("Characters:"));

        characterGroup = new ButtonGroup();

        for (String c:Deck.allCharacters){
            // options
            JRadioButton radioBtn = new JRadioButton(c);
            // button group
            characterGroup.add(radioBtn);

            buttonPanel.add(radioBtn);

            this.characterSelButtons.put(c,radioBtn);
        }


    }

    /**
     * set up the weapon radio boxes
     * @param buttonPanel current panel
     */
    public void setWeapons(JPanel buttonPanel){
        buttonPanel.add(new JLabel("Weapons:"));

        weaponGroup = new ButtonGroup();

        for (String c:Deck.allWeapons){
            // options
            JRadioButton radioBtn = new JRadioButton(c);
            // button group
            weaponGroup.add(radioBtn);

            buttonPanel.add(radioBtn);

            this.characterSelButtons.put(c,radioBtn);
        }

    }

    /**
     * set up the room radio boxes
     * @param buttonPanel current panel
     */
    public void setRooms(JPanel buttonPanel){
        buttonPanel.add(new JLabel("Estates:"));

        roomGroup = new ButtonGroup();

        for (String c:Deck.allRooms){
            // options
            JRadioButton radioBtn = new JRadioButton(c);
            // button group
            roomGroup.add(radioBtn);

            buttonPanel.add(radioBtn);

            this.characterSelButtons.put(c,radioBtn);
        }

    }

    /**
     * Enable / disable all options
     * @param allEnable - true for enable, false for disable
     */
    public void setAllEnable(Boolean allEnable){
        ButtonGroup[] lists;
        if (guess) {
            lists = new ButtonGroup[]{characterGroup, weaponGroup};
        } else {
            lists = new ButtonGroup[]{characterGroup, weaponGroup, roomGroup};
        }

        for(ButtonGroup group : lists) {
            for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements(); ) {
                AbstractButton button = buttons.nextElement();
                button.setEnabled(allEnable);

            }

        }
        guessButton.setEnabled(allEnable);
    }

    /**
     * Characters/weapons which have already been assigned to the current player should not be selectable.
     * (They will be grayed out).
     * @param player current
     */
    public void switchPlayer(Player player){

        for (Card c:player.getHand()){

            if(c.getClassification().equals(Card.type.Player) ){
                // disable the button
                this.characterSelButtons.get(c.getId()).setEnabled(false);
            }

        }
    }
    /**
     * get the characterButtonGroup
     * @return - characterButtonGroup
     */
    public ButtonGroup getCharacterGroup() {
        return characterGroup;
    }
    /**
     * get the roomButtonGroup
     * @return - roomButtonGroup
     */
    public ButtonGroup getRoomGroup() {
        return roomGroup;
    }
    /**
     * get the weaponButtonGroup
     * @return - weaponButtonGroup
     */
    public ButtonGroup getWeaponGroup() {
        return weaponGroup;
    }

    /**
     * get the selected button text
     * @param buttonGroup - selected button
     * @return - the text of the selected button
     */
    public String getSelectedButton(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

    /**
     * clear all selections on guess panel
     */
    public void unselectAllButtons(){
        for(ButtonGroup group : new ButtonGroup[]{characterGroup, weaponGroup, roomGroup}) {
            group.clearSelection();

        }

    }

    /**
     * set the guess state
     * @param value - isGuess
     */
    public void setGuess(boolean value) {
        guess = value;
    }
}
