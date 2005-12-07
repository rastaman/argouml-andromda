<?xml version="1.0" encoding="utf-8"?>
<!--
	Author: L.Maitre
	File: Config2Swing.xsl
	Date: 2005/12/05
	Purpose: Convert an andromda config file to a Swing form.
-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
>
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	<xsl:strip-space elements="*" />
	
	<xsl:template match="/">
<frame bundle="org.argouml.i18n.andromda">
	<panel>
		<xsl:apply-templates select="*"/>
	</panel>
</frame>
	</xsl:template>

<xsl:template match="namespaces|repositories|server">
	<vbox>
		<xsl:attribute name="border">TitledBorder(<xsl:value-of select="name()"/>)</xsl:attribute>
		<xsl:apply-templates/>
	</vbox>
</xsl:template>

<xsl:template match="namespace/properties">
	<vbox>
		<xsl:attribute name="border">TitledBorder(Properties for <xsl:value-of select="../@name"/>)</xsl:attribute>
		<xsl:apply-templates/>
	</vbox>
</xsl:template>

<xsl:template match="host|port|repository/models/model//*[text()!='']">
<xsl:variable name="property-id"><xsl:call-template name="getXPath"/></xsl:variable>
<hbox>
	<label>
		<xsl:attribute name="labelfor"><xsl:value-of select="$property-id"/></xsl:attribute>
		<xsl:attribute name="text"><xsl:value-of select="name()"/></xsl:attribute>		
	</label>
	<textfield>
		<xsl:attribute name="id"><xsl:value-of select="$property-id"/></xsl:attribute>
<xsl:attribute name="text"><xsl:value-of select="."/></xsl:attribute>
	</textfield>
</hbox>
</xsl:template>

<xsl:template match="property">
	<xsl:variable name="property-id"><xsl:call-template name="getXPath"/></xsl:variable>
	<hbox>
		<label>
			<xsl:attribute name="labelfor"><xsl:value-of select="$property-id"/></xsl:attribute>
			<xsl:attribute name="text"><xsl:value-of select="@name"/></xsl:attribute>		
		</label>
		<textfield>
			<xsl:attribute name="id"><xsl:value-of select="$property-id"/></xsl:attribute>
			<xsl:attribute name="text"><xsl:value-of select="."/></xsl:attribute>
		</textfield>
	</hbox>
</xsl:template>

<xsl:template name="getBeanName"><xsl:for-each select="ancestor::node()[name() != '']"><xsl:call-template name="getSimpleBeanName"/>.</xsl:for-each><xsl:call-template name="getSimpleBeanName"/></xsl:template>

<xsl:template name="getXPath">/<xsl:for-each select="ancestor::node()[name() != '']"><xsl:call-template name="getSimpleBeanName"/>/</xsl:for-each><xsl:call-template name="getSimpleBeanName"/></xsl:template>

<xsl:template name="getSimpleBeanName">
<xsl:variable name="name"><xsl:value-of select="name()"/></xsl:variable>
<xsl:value-of select="name()"/>
<xsl:if test="count(../*[name()=$name]) &gt; 1">[<xsl:value-of select="count(preceding-sibling::node()[name()=$name])+1"/>]</xsl:if>
</xsl:template>
 
 <xsl:template name="capitalize">
  <xsl:param name="aString"/>
  <xsl:value-of select="translate(substring($aString,1,1),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/><xsl:value-of select="substring($aString,2,string-length($aString))"/>
 </xsl:template>
 
 <xsl:template name="uncapitalize">
  <xsl:param name="aString"/>
  <xsl:value-of select="translate(substring($aString,1,1),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/><xsl:value-of select="substring($aString,2,string-length($aString))"/>
 </xsl:template>

<!-- Id xsl:
<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>
-->

</xsl:stylesheet>
