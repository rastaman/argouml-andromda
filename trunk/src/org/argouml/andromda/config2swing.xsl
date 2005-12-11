<?xml version="1.0" encoding="utf-8"?>
<!--
    Author: L.Maitre
    File: Config2Swing.xsl
    Date: 2005/12/05
    Purpose: Convert an andromda config file to a Swing form.
-->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xsl:output method="xml" indent="yes" encoding="utf-8" />
    <xsl:strip-space elements="*" />

    <xsl:param name="namespace">default</xsl:param>
    
    <xsl:template match="/">
        <panel bundle="org.argouml.i18n.andromda" minimumSize="480,700"
            maximumSize="480,700" preferredSize="480,700"
            size="480,700" layout="FlowLayout(FlowLayout.CENTER)">
            <vbox>
            <panel
                 visible="true"
                minimumSize="480,60" maximumSize="480,60"
                preferredSize="480,60" size="480,60">
                <xsl:apply-templates select="//repositories|//server" />
            </panel>
            <panel minimumSize="480,512" maximumSize="480,512"
                preferredSize="480,512" size="480,512"
                defaultCloseOperation="JFrame.HIDE_ON_CLOSE"
                layout="FlowLayout(FlowLayout.LEFT)">
                <scrollpane autoscrolls="true">
                <xsl:apply-templates select="//namespaces" />
                </scrollpane>
            </panel>
            </vbox>
            <panel id="andromda:config:buttons" visible="false">
                <button id="andromda:config:apply" text="text.apply"
                    action="andromda:config:action:apply" />
                <button id="andromda:config:close" text="text.close"
                    action="andromda:config:action:close" />
            </panel>
        </panel>
    </xsl:template>

    <xsl:template match="repositories|server">
        <vbox>
            <xsl:attribute name="border">TitledBorder(<xsl:value-of select="name()" />)</xsl:attribute>
            <xsl:apply-templates />
        </vbox>
    </xsl:template>

    <xsl:template match="namespaces">
        <vbox>
            <xsl:attribute name="border">TitledBorder(<xsl:value-of select="name()" />)</xsl:attribute>
            <itemList id="config:listmodel">
                <xsl:for-each select="namespace">
                    <item>
                        <xsl:attribute name="name">
                            <xsl:value-of select="@name"/>
                        </xsl:attribute>
                        <xsl:attribute name="value">
                            <xsl:call-template name="getXPath" />
                        </xsl:attribute>
                    </item>
                </xsl:for-each>
            </itemList>
            <combobox id="andromda:config:select-namespace" action="andromda:config:action:select-namespace"
                prototypeDisplayValue="1234567890|1234567890" 
                initclass="org.argouml.modules.gui.ComboModel(config:listmodel)" />
            <xsl:apply-templates
                select="namespace/properties" />
        </vbox>
    </xsl:template>

    <xsl:template match="namespace/properties">
        <vbox>
            <xsl:attribute name="id">namespaces:properties:<xsl:value-of select="../@name"/></xsl:attribute>
            <xsl:attribute name="visible">
            <xsl:choose>
            <xsl:when test="../@name=$namespace">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
            </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="border">TitledBorder(Properties for <xsl:value-of select="../@name" />)</xsl:attribute>
            <xsl:apply-templates />
        </vbox>
    </xsl:template>

    <xsl:template
        match="host|port|repository/models/model//*[text()!='']">
        <xsl:variable name="property-id">
            <xsl:call-template name="getXPath" />
        </xsl:variable>
        <hbox>
            <label>
                <xsl:attribute name="labelfor">
                    <xsl:value-of select="$property-id" />
                </xsl:attribute>
                <xsl:attribute name="text">
                    <xsl:value-of select="name()" />
                </xsl:attribute>
            </label>
            <textfield>
                <xsl:attribute name="id">
                    <xsl:value-of select="$property-id" />
                </xsl:attribute>
                <xsl:attribute name="text">
                    <xsl:value-of select="." />
                </xsl:attribute>
            </textfield>
        </hbox>
    </xsl:template>

    <xsl:template match="property">
        <xsl:variable name="property-id">
            <xsl:call-template name="getXPath" />
        </xsl:variable>
        <hbox>
            <label>
                <xsl:attribute name="labelfor">
                    <xsl:value-of select="$property-id" />
                </xsl:attribute>
                <xsl:attribute name="text">
                    <xsl:value-of select="@name" />
                </xsl:attribute>
            </label>
            <textfield>
                <xsl:attribute name="id">
                    <xsl:value-of select="$property-id" />
                </xsl:attribute>
                <xsl:attribute name="text">
                    <xsl:value-of select="." />
                </xsl:attribute>
            </textfield>
        </hbox>
    </xsl:template>

    <xsl:template name="getBeanName">
        <xsl:for-each select="ancestor::node()[name() != '']">
            <xsl:call-template name="getSimpleBeanName" />
            .
        </xsl:for-each>
        <xsl:call-template name="getSimpleBeanName" />
    </xsl:template>

    <xsl:template name="getXPath">
        <xsl:text>/</xsl:text>
        <xsl:for-each select="ancestor::node()[name() != '']"><xsl:call-template name="getSimpleBeanName" />/</xsl:for-each>
        <xsl:call-template name="getSimpleBeanName" />
    </xsl:template>

    <xsl:template name="getSimpleBeanName">
        <xsl:variable name="name"><xsl:value-of select="name()" /></xsl:variable>
        <xsl:value-of select="name()" />
        <xsl:if test="count(../*[name()=$name]) &gt; 1">[<xsl:value-of select="count(preceding-sibling::node()[name()=$name])+1" />]</xsl:if>
    </xsl:template>

    <xsl:template name="capitalize">
        <xsl:param name="aString" />
        <xsl:value-of
            select="translate(substring($aString,1,1),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')" />
        <xsl:value-of
            select="substring($aString,2,string-length($aString))" />
    </xsl:template>

    <xsl:template name="uncapitalize">
        <xsl:param name="aString" />
        <xsl:value-of
            select="translate(substring($aString,1,1),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" />
        <xsl:value-of
            select="substring($aString,2,string-length($aString))" />
    </xsl:template>

    <!-- Id xsl:
        <xsl:template match="@*|node()">
        <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
        </xsl:template>
    -->

</xsl:stylesheet>
