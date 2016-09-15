package kalei.com.learnwithme.models;

/**
 * Created by risaki on 9/14/16.
 */
public class Letter {

    public Letter(final String letterCharacter, final boolean isUnderlined, final int indexOfLetter) {
        this.letterCharacter = letterCharacter;
        this.isUnderlined = isUnderlined;
        this.indexOfLetter = indexOfLetter;
    }

    public String getLetterCharacter() {
        return letterCharacter;
    }

    public void setLetterCharacter(final String letterCharacter) {
        this.letterCharacter = letterCharacter;
    }

    String letterCharacter;

    public boolean isUnderlined() {
        return isUnderlined;
    }

    public void setUnderlined(final boolean underlined) {
        isUnderlined = underlined;
    }

    boolean isUnderlined;

    public int getIndexOfLetter() {
        return indexOfLetter;
    }

    public void setIndexOfLetter(final int indexOfLetter) {
        this.indexOfLetter = indexOfLetter;
    }

    int indexOfLetter;
}
