package com.svo.library.widget.file_choose.atree.node;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.svo.library.widget.file_choose.atree.BaseMutableTreeNode;

public abstract class BaseFileTreeNode extends BaseMutableTreeNode implements Comparable<BaseFileTreeNode> {
	public static final String TAG = BaseFileTreeNode.class.getSimpleName();
	protected final File file;
	protected final File[] virtualChildren;

	public BaseFileTreeNode(File file) {
		this.file = file;
		this.virtualChildren = null;
	}

	public BaseFileTreeNode(File[] virtualChildren) {
		this.file = null;
		this.virtualChildren = virtualChildren;
	}

	@Override public void setExpanded(boolean expanded) {
		super.setExpanded(expanded);

		if (expanded) {
			File[] files;
			if (file != null && file.isDirectory()) {
				files = file.listFiles(fileFilter);
				if (files != null) Arrays.sort(files, fileComparator);
			} else if (virtualChildren != null) {
				files = virtualChildren;
			} else {
				files = null;
			}
			
			if (files == null) {
				this.removeAllChildren();
			} else {
				HashMap<String, BaseFileTreeNode> existing = new HashMap<String, BaseFileTreeNode>();
				for (int i = 0; i < this.getChildCount(); i++) {
					BaseFileTreeNode child = this.getChildAt(i);
					existing.put(child.file.getName(), child);
				}
				
				this.removeAllChildren();
				
				for (File file : files) {
					BaseFileTreeNode existingNode = existing.get(file.getName());
					if (existingNode != null) {
						this.add(existingNode);
					} else {
						this.add(generateForFile(file));
					}
				}
			}
		}
	}
	
	private FileFilter fileFilter = new FileFilter() {
		@Override public boolean accept(File pathname) {
			if (showDirectoriesOnly()) {
				if (!pathname.isDirectory()) {
					return false;
				}
			}
			
			if (!showHidden()) {
				if (pathname.isHidden()) {
					return false;
				}
			}
			
			return true;
		}
	};
	
	static Comparator<File> fileComparator = new Comparator<File>() {
		@Override public int compare(File a, File b) {
			// virtual first
			if (a == null) {
				return -1;
			} else if (b == null) {
				return +1;
			}
			
			if (a.isDirectory() && !b.isDirectory()) {
				return -1;
			} else if (!a.isDirectory() && b.isDirectory()) {
				return +1;
			}
			
			// both files or both dirs
			
			String aname = a.getName();
			String bname = b.getName();
			
			// dot-files are later
			if (aname.startsWith(".") && !bname.startsWith(".")) { //$NON-NLS-1$ //$NON-NLS-2$
				return +1;
			} else if (!aname.startsWith(".") && bname.startsWith(".")) { //$NON-NLS-1$ //$NON-NLS-2$
				return -1;
			}
			
			return aname.compareToIgnoreCase(bname);
		}
	};

	@Override public int compareTo(BaseFileTreeNode another) {
		File a = this.file;
		File b = another.file;
		return fileComparator.compare(a, b);
	}

	protected abstract BaseFileTreeNode generateForFile(File file);
	
	protected boolean showDirectoriesOnly() {
		return false;
	}
	
	protected boolean showHidden() {
		return true;
	}

	public File getFile() {
		return file;
	}
	
	public File[] getVirtualChildren() {
		return virtualChildren;
	}
}
