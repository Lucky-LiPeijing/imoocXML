package com.jyj.FSImage;

import com.jyj.entity.Block;
import com.jyj.entity.Directory;
import com.jyj.entity.Inode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Classname Utils
 * @Description TODO
 * @Date 2019/2/28 11:57
 * @Created by lipeijing
 */
public class Utils {

    public static SAXReader READER = new SAXReader();

    public Element getRootElement(String filePath) {
        Element element = null;
        try {
            Document document = READER.read(new File(filePath));
            element = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("Document创建失败，查看文件路径是否正确");
        }
        return element;
    }

    public void getElementContext (Element element) {
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element elm = (Element) iterator.next();
            System.out.println(elm.getName() + ":" + elm.getStringValue());
        }
    }

    public List getINodeDirectorySection (Element element) {
        ArrayList<Directory> directories = new ArrayList<Directory>();
        ArrayList<String> inodeIds = null;
        Directory directory = null;
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element elm = (Element) iterator.next();
            Iterator iter = elm.elementIterator();
            directory = new Directory();
            inodeIds = new ArrayList<String>();
            while (iter.hasNext()) {
                Element elem = (Element) iter.next();
                String elemName = elem.getName();
                if (elemName.equals("parent")) {
                    directory.setParent(elem.getStringValue());
                } else if (elemName.equals("inode")) {
                    inodeIds.add(elem.getStringValue());
                }
            }
            directory.setInodes(inodeIds);
            directories.add(directory);
            directory = null;
            inodeIds = null;
        }
        return directories;
    }

    public List getINodeSection (Element element) {
        Iterator iterator = element.elementIterator();
        List<Inode> inodes = new ArrayList<Inode>();
        List<Block> blocks = null;
        Inode inode = null;
        Block block = null;
        while (iterator.hasNext()) {
            Element elm = (Element) iterator.next();
            if (elm.getName().equals("lastInodeId")) {

            } else {
                Iterator iter = elm.elementIterator();
                inode = new Inode();
                while (iter.hasNext()) {
                    Element elem = (Element) iter.next();
                    if (elem.getName().equals("blocks")) {
                        Iterator itera = elem.elementIterator();
                        blocks = new ArrayList<Block>();
                        while (itera.hasNext()) {
                            Element elemen = (Element) itera.next();
                            Iterator iterat = elemen.elementIterator();
                            block = new Block();
                            while (iterat.hasNext()) {
                                Element element1 = (Element) iterat.next();
                                if (elemen.getName().equals("id")) {
                                    block.setId(elemen.getStringValue());
//                                System.out.println(block.getId());
                                } else if (elemen.getName().equals("genstamp")) {
                                    block.setGenstamp(elemen.getStringValue());
                                } else if (elemen.getName().equals("numBytes")) {
                                    block.setNumBytes(elemen.getStringValue());
                                }
                            }
                            blocks.add(block);
                            block = null;
                        }
                        inode.setBlocks(blocks);
                        blocks = null;
                    } else {
                        if (elem.getName().equals("id")) {
                            inode.setId(elem.getStringValue());
                        } else if (elem.getName().equals("type")) {
                            inode.setType(elem.getStringValue());
                        } else if (elem.getName().equals("name")) {
                            inode.setName(elem.getStringValue());
                        } else if (elem.getName().equals("replication")) {
                            inode.setReplication(Integer.parseInt(elem.getStringValue()));
                        } else if (elem.getName().equals("mtime")) {
                            inode.setMtime(elem.getStringValue());
                        } else if (elem.getName().equals("atime")) {
                            inode.setAtime(elem.getStringValue());
                        } else if (elem.getName().equals("perferredBlockSize")) {
                            inode.setPerferredBlockSize(elem.getStringValue());
                        } else if (elem.getName().equals("permission")) {
                            inode.setPermission(elem.getStringValue());
                        } else if (elem.getName().equals("nsquota")) {
                            inode.setNsquota(elem.getStringValue());
                        } else if (elem.getName().equals("dsquota")) {
                            inode.setDsquota(elem.getStringValue());
                        }
                    }
                }
            }
        }
        return inodes;
    }
}