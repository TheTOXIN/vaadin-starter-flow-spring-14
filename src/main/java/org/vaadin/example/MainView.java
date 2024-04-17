package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Route
public class MainView extends VerticalLayout {

    public static final String FILE_URL = "https://freetestdata.com/wp-content/uploads/2024/01/sample2zip.rar";

    private Test selectedTest;

    public MainView() {
        Anchor test1 = new Anchor(FILE_URL, "download test 1");
        test1.getElement().setAttribute("download", true);

        Button test2 = new Button("download test 2");
        test2.addClickListener(e ->
                test2.getElement().executeJs("window.location.href = $0", FILE_URL));

        HorizontalLayout buttons = new HorizontalLayout(test1, test2);

        Tab tab1 = new Tab("TAB 1");
        Tab tab2 = new Tab("TAB 2");
        Tab tab3 = new Tab("TAB 3");

        VerticalLayout content = new VerticalLayout();
        VerticalLayout page1 = new VerticalLayout(new Text("PAGE 1"), buttons, makeGrid());
        VerticalLayout page2 = new VerticalLayout(new Text("PAGE 2"));
        VerticalLayout page3 = new VerticalLayout(new Text("PAGE 3"));
        content.setWidthFull();
        content.add(page1);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        tabsToPages.put(tab3, page3);

        Tabs tabs = new Tabs(tab1, tab2, tab3);
        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            content.add(tabsToPages.get(event.getSelectedTab()));
        });

        add(tabs, content);
    }

    private Component makeGrid() {
        Grid<Test> grid = new Grid<>(Test.class);

        grid.setItems(
                IntStream.range(0, 5)
                        .mapToObj(i -> new Test("a" + i, "b" + i, "c" + i))
                        .collect(Collectors.toList())
        );

        grid.removeAllColumns();
        grid.setWidthFull();

        grid.addColumn("a");
        grid.addColumn("b");
        grid.addColumn("c");

        grid.addSelectionListener(e -> {
            e.getFirstSelectedItem().ifPresent(test -> {
                selectedTest = test;
            });
        });

        Button showTest = new Button("OPEN TEST", e -> {
            Notification.show(selectedTest != null ? selectedTest.toString() : "empty");
        });

        return new VerticalLayout(grid, showTest);
    }

    public static class Test {
        private String a;
        private String b;
        private String c;

        public Test(final String a, final String b, final String c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public String getA() {
            return a;
        }

        public void setA(final String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(final String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(final String c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "Test{" +
                    "a='" + a + '\'' +
                    ", b='" + b + '\'' +
                    ", c='" + c + '\'' +
                    '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Test test = (Test) o;
            return Objects.equals(a, test.a) && Objects.equals(b, test.b) && Objects.equals(c, test.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }
}
