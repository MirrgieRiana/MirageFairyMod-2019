package miragefairy2019.colormaker.core;

import kotlin.Unit;

import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import static mirrg.boron.swing.UtilsComponent.get;

public class PanelSliderField extends JPanel {

    private JSlider slider;
    private ParsingTextField<Integer> textField;

    private boolean isInProcessing = false;

    public PanelSliderField() {

        setLayout(get(new GridBagLayout(), l -> {
            l.columnWidths = new int[]{300, 50};
            l.rowHeights = new int[]{0};
            l.columnWeights = new double[]{0.0, 0.0};
            l.rowWeights = new double[]{0.0};
        }));

        add(get(slider = new JSlider(), c -> {
            c.setMajorTickSpacing(8);
            c.setPaintTicks(true);
            c.setMaximum(255);
            c.addChangeListener(e -> {
                if (isInProcessing) return;
                setValue(c.getValue(), c);
            });
        }), get(new GridBagConstraints(), c -> {
            c.insets = new Insets(0, 0, 0, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
        }));

        Pattern pattern = Pattern.compile("[0-9]+");
        add(get(textField = new ParsingTextField<>(s -> {
            int i;
            if (pattern.matcher(s.trim()).matches()) {
                try {
                    i = Integer.parseInt(s.trim(), 10);
                } catch (Exception e) {
                    return null;
                }
                if (i < slider.getMinimum()) return null;
                if (i > slider.getMaximum()) return null;
                return i;
            } else {
                return null;
            }
        }, v -> "" + v), c -> {
            c.setColumns(5);
            c.getListeners().add(i -> {
                if (isInProcessing) return Unit.INSTANCE;
                setValue(i, c);
                return Unit.INSTANCE;
            });
        }), get(new GridBagConstraints(), c -> {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
        }));

        //

        setValue(0);

    }

    //

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        setValue(value, null);
    }

    //

    public ArrayList<IntConsumer> listeners = new ArrayList<>();

    private void setValue(int value, Object source) {
        isInProcessing = true;

        this.value = value;
        if (source != slider) slider.setValue(value);
        if (source != textField) textField.setValue(value);
        listeners.forEach(l -> {
            try {
                l.accept(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        isInProcessing = false;
    }

}
