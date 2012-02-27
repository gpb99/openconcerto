/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 OpenConcerto, by ILM Informatique. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the GNU General Public License Version 3
 * only ("GPL"). You may not use this file except in compliance with the License. You can obtain a
 * copy of the License at http://www.gnu.org/licenses/gpl-3.0.html See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each file.
 */
 
 package org.openconcerto.openoffice.spreadsheet;

import org.openconcerto.openoffice.LengthUnit;
import org.openconcerto.openoffice.ODPackage;
import org.openconcerto.openoffice.StyleStyle;
import org.openconcerto.openoffice.StyleStyleDesc;
import org.openconcerto.openoffice.XMLVersion;
import org.openconcerto.openoffice.style.SideStyleProperties;

import java.math.BigDecimal;
import java.util.Arrays;

import org.jdom.Element;

public class TableStyle extends StyleStyle {

    static public final LengthUnit DEFAULT_UNIT = LengthUnit.MM;
    // from section 18.728 in v1.2-part1
    public static final StyleStyleDesc<TableStyle> DESC = new StyleStyleDesc<TableStyle>(TableStyle.class, XMLVersion.OD, "table", "ta", "table", Arrays.asList("table:background", "table:table")) {
        @Override
        public TableStyle create(ODPackage pkg, Element e) {
            return new TableStyle(pkg, e);
        }
    };

    private StyleTableProperties tableProps;

    public TableStyle(final ODPackage pkg, Element tableColElem) {
        super(pkg, tableColElem);
    }

    public final StyleTableProperties getTableProperties() {
        if (this.tableProps == null)
            this.tableProps = new StyleTableProperties(this);
        return this.tableProps;
    }

    public final Float getWidth() {
        final BigDecimal width = getTableProperties().getWidth(TableStyle.DEFAULT_UNIT);
        return width == null ? null : width.floatValue();
    }

    void setWidth(float f) {
        getFormattingProperties().setAttribute("width", f + DEFAULT_UNIT.getSymbol(), this.getSTYLE());
    }

    // see 17.15 of v1.2-cs01-part1
    public static class StyleTableProperties extends SideStyleProperties {

        public StyleTableProperties(StyleStyle style) {
            super(style, style.getFamily());
        }

        public final String getRawMargin(final Side s) {
            return getSideAttribute(s, "margin", this.getNS("fo"));
        }

        /**
         * Get the margin of one of the side.
         * 
         * @param s which side.
         * @param in the desired unit.
         * @return the margin.
         */
        public final BigDecimal getMargin(final Side s, final LengthUnit in) {
            return LengthUnit.parseLength(getRawMargin(s), in);
        }

        public final BigDecimal getWidth(final LengthUnit in) {
            return LengthUnit.parseLength(getAttributeValue("width", this.getNS("style")), in);
        }
    }
}