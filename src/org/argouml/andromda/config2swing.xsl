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
        <panel bundle="org.argouml.i18n.andromda" minimumSize="640,480"
            maximumSize="640,480" preferredSize="640,480" size="640,480">
            <vbox>
                <panel>
                    <xsl:apply-templates select="//repositories|//server" />
                </panel>
                <xsl:apply-templates select="//namespaces" />
            </vbox>
            <panel id="andromda:config:buttons" visible="false">
                <button id="andromda:config:apply" text="text.apply"
                    action="andromda:config:action:apply" />
                <!-- 
                <button id="andromda:config:close" text="text.close"
                    action="andromda:config:action:close" />
                     -->
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
        <panel minimumSize="630,400" maximumSize="630,400"
            preferredSize="630,400" size="630,400">
            <itemList id="config:listmodel">
                <xsl:for-each select="namespace">
                    <item>
                        <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
                        <xsl:attribute name="value"><xsl:call-template name="getXPath" /></xsl:attribute>
                    </item>
                </xsl:for-each>
            </itemList>
            <combobox id="andromda:config:select-namespace"
                action="andromda:config:action:select-namespace"
                prototypeDisplayValue="1234567890|1234567890"
                initclass="org.argouml.modules.gui.ComboModel(config:listmodel)" />
            <xsl:apply-templates select="namespace/properties" />
        </panel>
    </xsl:template>

    <xsl:template match="namespace/properties">
        <scrollpane minimumSize="626,360" maximumSize="626,360"
            preferredSize="626,360" size="626,360">
                <xsl:attribute name="id">namespaces:properties:<xsl:value-of select="../@name" /></xsl:attribute>
                <xsl:attribute name="visible">
                    <xsl:choose>
                        <xsl:when test="../@name=$namespace">true</xsl:when>
                        <xsl:otherwise>false</xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <vbox>
                    <xsl:attribute name="border">TitledBorder(Properties for <xsl:value-of select="../@name" />)</xsl:attribute>
                    <xsl:apply-templates />
                </vbox>
        </scrollpane>
    </xsl:template>

    <xsl:template
        match="host|port|repository/models/model//*[text()!='']">
        <xsl:variable name="property-id"><xsl:call-template name="getXPath" /></xsl:variable>
        <hbox>
            <label>
                <xsl:attribute name="labelfor"><xsl:value-of select="$property-id" /></xsl:attribute>
                <xsl:attribute name="text"><xsl:value-of select="name()" /></xsl:attribute>
            </label>
            <textfield>
                <xsl:attribute name="id"><xsl:value-of select="$property-id" /></xsl:attribute>
                <xsl:attribute name="text"><xsl:value-of select="." /></xsl:attribute>
            </textfield>
        </hbox>
    </xsl:template>

    <xsl:template match="property">
        <xsl:variable name="property-id">
            <xsl:call-template name="getXPath" />
        </xsl:variable>
        <hbox minimumSize="610,20" maximumSize="610,20"
            preferredSize="610,20" size="610,20">
            <label>
                <xsl:attribute name="labelfor"><xsl:value-of select="$property-id" /></xsl:attribute>
                <xsl:attribute name="text"><xsl:value-of select="@name" /></xsl:attribute>
            </label>
            <textfield>
                <xsl:attribute name="id"><xsl:value-of select="$property-id" /></xsl:attribute>
                <xsl:attribute name="text"><xsl:value-of select="." /></xsl:attribute>
            </textfield>
        </hbox>
    </xsl:template>

    <xsl:template name="getBeanName">
        <xsl:for-each select="ancestor::node()[name() != '']"><xsl:call-template name="getSimpleBeanName" />.</xsl:for-each>
        <xsl:call-template name="getSimpleBeanName" />
    </xsl:template>

    <xsl:template name="getXPath">
        <xsl:text>/</xsl:text>
        <xsl:for-each select="ancestor::node()[name() != '']"><xsl:call-template name="getSimpleBeanName" />/</xsl:for-each>
        <xsl:call-template name="getSimpleBeanName" />
    </xsl:template>

    <xsl:template name="getSimpleBeanName">
        <xsl:variable name="name">
            <xsl:value-of select="name()" />
        </xsl:variable>
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
