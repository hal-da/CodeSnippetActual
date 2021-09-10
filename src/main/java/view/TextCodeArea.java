package view;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import util.Constants;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextCodeArea {

    public static String[] KEYWORDS = Constants.STANDARD_KEY_WORDS;

    static String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String ASSIGNMENT_PATTERN = "\\s+\\w+?\\s+=" + "|" + "\\s+\\w+\\[.*\\]?\\s+=";

    private Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<ASSIGNMENT>" + ASSIGNMENT_PATTERN + ")"
    );

    public static void setKeywordPattern(){
        KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    }

    public void compilePattern(){
        PATTERN =  Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                        + "|(?<ASSIGNMENT>" + ASSIGNMENT_PATTERN + ")"
        );
    }

    private final CodeArea codeArea;
    Subscription cleanupWhenNoLongerNeedIt;

    public  TextCodeArea(AnchorPane pane, String s, String[] keywords) {
        setKEYWORDS(keywords);
        compilePattern();
        codeArea = new CodeArea();
        codeArea.appendText(s);
        VirtualizedScrollPane<CodeArea> sp = new VirtualizedScrollPane<>(codeArea);
        pane.getChildren().add(sp);
        AnchorPane.setLeftAnchor(sp, 0.0);
        AnchorPane.setRightAnchor(sp, 0.0);
        AnchorPane.setBottomAnchor(sp, 0.0);
        AnchorPane.setTopAnchor(sp, 0.0);
        codeArea.prefWidthProperty().bind(pane.widthProperty());
        codeArea.prefHeightProperty().bind(pane.heightProperty());

        codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));

        cleanupWhenNoLongerNeedIt =  codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(50))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );


        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.ENTER) {
                int pos = codeArea.getCaretPosition();
                int par = codeArea.getCurrentParagraph();
                Matcher matcher = whiteSpace.matcher(codeArea.getParagraph(par-1).getSegments().get(0));
                if (matcher.find()) Platform.runLater(() -> codeArea.insertText(pos, matcher.group()));
            }
        });
    }

    public void cleanUp(){
        cleanupWhenNoLongerNeedIt.unsubscribe();
    }
    public String getText(){
        return codeArea.getText();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        int lastKwEnd = 0;
        Matcher matcher = PATTERN.matcher(text);
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            matcher.group("ASSIGNMENT") != null ? "assignment" :
                                                                                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public String[] getKEYWORDS() {
        return KEYWORDS;
    }

    public void setKEYWORDS(String[] KEYWORDS) {
        this.KEYWORDS = KEYWORDS;
        setKeywordPattern();
    }
}
