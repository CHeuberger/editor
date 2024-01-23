package cfh.editor;

import static java.util.Objects.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
    
    private final World world;

    private JTree toolbox;
    private JPanel overview;
    private CompoundCanvas model;
    
    
    public EditorPanel(World world) {
        this.world = requireNonNull(world);
        
        model = new CompoundCanvas(world.root());

        TNode rootTool = createTools(model);
        toolbox = newTree(rootTool);
        toolbox.setEditable(false);
        toolbox.setMinimumSize(new Dimension(200, 200));
        toolbox.setRootVisible(false);
        toolbox.setShowsRootHandles(true);
        toolbox.setToggleClickCount(1);
        toolbox.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        toolbox.addTreeSelectionListener(this::doToolSelected);
        toolbox.setSelectionInterval(0, 0);
        expandTree(toolbox, new TreePath(rootTool));
        
        overview = newPanel();
        
        JSplitPane leftSplit = newSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        JScrollPane toolPane = newScrollPane(toolbox);
        toolPane.setMinimumSize(new Dimension(200, 200));
        leftSplit.setTopComponent(toolPane);
        leftSplit.setBottomComponent(newScrollPane(overview));
        
        JSplitPane mainSplit = newSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setLeftComponent(leftSplit);
        mainSplit.setRightComponent(model);
        
        setLayout(new BorderLayout());
        add(mainSplit, BorderLayout.CENTER);
    }
    
    private void expandTree(JTree tree, TreePath path) {
        tree.expandPath(path);
        Object node = path.getLastPathComponent();
        if (node instanceof GroupNode) {
            ((GroupNode) node).children.forEach(n -> expandTree(tree, path.pathByAddingChild(n)));
        }
    }

    private void doToolSelected(TreeSelectionEvent ev) {
        TNode node = (TNode) ev.getPath().getLastPathComponent();
        System.out.println(node);
        if (node instanceof ToolNode) {
            model.tool(((ToolNode)node).tool);
        } else {
            model.tool(null);
        }
    }
    
    private TNode createTools(CompoundCanvas canvas) {
        GroupNode root = new GroupNode(null, "tools");
        for (Tool tool : world.tools()) {
            List<String> path = tool.path();
            GroupNode node = root.getOrCreate(path);
            new ToolNode(node, tool);
            tool.canvas(canvas);
        }
        return root;
    }
    
    private JPanel newPanel() {
        JPanel panel = new JPanel();
        return panel;
    }
    
    private JTree newTree(TreeNode root) {
        JTree tree = new JTree(root);
        return tree;
    }
    
    private JSplitPane newSplitPane(int orientation) {
        JSplitPane split = new JSplitPane(orientation);
        split.setOneTouchExpandable(true);
        return split;
    }
    
    private JScrollPane newScrollPane(Component view) {
        JScrollPane pane = new JScrollPane(view);
        return pane;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    private static abstract class TNode implements TreeNode {

        private final String name;
        private final GroupNode parent;
        
        protected TNode(GroupNode parent, String name) {
            this.name = requireNonNull(name);
            this.parent = parent;
            if (parent != null) {
                parent.add(this);
            }
        }
        
        String name() {
            return name;
        }

        @Override
        public TreeNode getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    private static class GroupNode extends TNode {
        
        private final List<TNode> children = new ArrayList<>();

        GroupNode(GroupNode parent, String name) {
            super(parent, name);
        }
        
        void add(TNode node) {
            children.add(node);
        }
        
        TNode child(String name) {
            for (TNode child : children) {
                if (child.name().equals(name))
                    return child;
            }
            return null;
        }
        
        GroupNode getOrCreate(List<String> path) {
            if (path == null || path.isEmpty())
                return this;
            
            String name = path.get(0);
            TNode node = child(name);
            if (node == null) {
                node = new GroupNode(this, name);
            }
            return ((GroupNode) node).getOrCreate(path.subList(1, path.size()));
        }
        
        @Override
        public TreeNode getChildAt(int index) {
            return children.get(index);
        }

        @Override
        public int getChildCount() {
            return children.size();
        }

        @Override
        public int getIndex(TreeNode node) {
            return children.indexOf(node);
        }

        @Override
        public boolean getAllowsChildren() {
            return true;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public Enumeration<TNode> children() {
            return Collections.enumeration(children);
        }
    }
    
    
    private static class ToolNode extends TNode {
        
        private final Tool tool;

        ToolNode(GroupNode parent, Tool tool) {
            super(parent, tool.name());
            this.tool = requireNonNull(tool);
        }
        
        @Override
        public TreeNode getChildAt(int childIndex) {
            return null;
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public int getIndex(TreeNode node) {
            return -1;
        }

        @Override
        public boolean getAllowsChildren() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public Enumeration<TNode> children() {
            return null;
        }
    }
}
