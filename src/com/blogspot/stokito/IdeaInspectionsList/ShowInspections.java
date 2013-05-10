package com.blogspot.stokito.IdeaInspectionsList;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.containers.ContainerUtil;
import com.sun.java.util.jar.pack.*;
import gnu.trove.THashSet;

import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;

public class ShowInspections extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Set<InspectionToolProvider> providers = new LinkedHashSet<InspectionToolProvider>();
        ContainerUtil.addAll(providers, ApplicationManager.getApplication().getComponents(InspectionToolProvider.class));
        String providersList = listCsv(providers);
        StringSelection stringSelection = new StringSelection(providersList);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Messages.showMessageDialog("All available inspections:\n" + providersList, "Information", Messages.getInformationIcon());
    }

    private String list(Set<InspectionToolProvider> providers) {
        String providersList = "";
        for (InspectionToolProvider provider : providers) {
            providersList += provider.getClass().getSimpleName() + "\n";
            Class[] inspections = provider.getInspectionClasses();
            for (Class inspection : inspections) {
                providersList += "\t" + inspection.getSimpleName();
                try {
                    InspectionProfileEntry inspectionTool = (InspectionProfileEntry) inspection.newInstance();
                    providersList += "<tr class=\"" + inspectionTool.getDefaultLevel() + "\" id=\"" + inspectionTool.getShortName() + "\">\n";
                    providersList += " <td>" + inspectionTool.getGroupDisplayName() + "</td>\n";
                    providersList += " <td>" + inspectionTool.getDisplayName() + "</td>\n";
                    providersList += " <td>" + inspectionTool.getStaticDescription() + "</td>\n";
                    providersList += "</tr>";
                } catch (Exception e) {
                    e.printStackTrace();    //TODO
                }
                providersList += "\n";
            }
        }
        return providersList;
    }

    private String listHtml(Set<InspectionToolProvider> providers) {
        String providersList = "";
        for (InspectionToolProvider provider : providers) {
            providersList += provider.getClass().getSimpleName() + "\n";
            Class[] inspections = provider.getInspectionClasses();
            for (Class inspection : inspections) {
                providersList += "\t" + inspection.getSimpleName();
                try {
                    InspectionProfileEntry inspectionTool = (InspectionProfileEntry) inspection.newInstance();
                    providersList += "<tr class=\"" + inspectionTool.getDefaultLevel() + "\" id=\"" + inspectionTool.getShortName() + "\">\n";
                    providersList += " <td>" + inspectionTool.getGroupDisplayName() + "</td>\n";
                    providersList += " <td>" + inspectionTool.getDisplayName() + "</td>\n";
                    providersList += " <td>" + inspectionTool.getStaticDescription() + "</td>\n";
                    providersList += "</tr>";
                } catch (Exception e) {
                    e.printStackTrace();    //TODO
                }
                providersList += "\n";
            }
        }
        return providersList;
    }

    private String listCsv(Set<InspectionToolProvider> providers) {
        String providersList = "";
        for (InspectionToolProvider provider : providers) {
            Class[] inspections = provider.getInspectionClasses();
            for (Class inspection : inspections) {
                InspectionProfileEntry inspectionTool;
                try {
                    inspectionTool = (InspectionProfileEntry) inspection.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();    //TODO
                    continue;
                }
                providersList += provider.getClass().getSimpleName() + ",";
                providersList += inspection.getSimpleName() + ",";
                providersList += inspectionTool.getDefaultLevel() + ",";
                providersList += inspectionTool.getShortName() + ",";
                providersList += inspectionTool.getGroupDisplayName() + ",";
                providersList += inspectionTool.getDisplayName() + ",";
                providersList += inspectionTool.getStaticDescription() + ",";
                providersList += "\n";
            }
        }
        return providersList;
    }
}
