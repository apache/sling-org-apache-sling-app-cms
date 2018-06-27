<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	you under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Editor Field Types

# Editor Field Types

The following editor field types are available in Sling CMS under the path `/libs/sling-cms/components/editor/fields`:

## Button 

Renders a button. 

**Resource Type:** `sling-cms/components/editor/fields/button`

**Properties**

- *onclick* - a Javascript method to execute when the button is clicked
- *label* - the text for the button

## File 

Renders multiple file upload fields.

**Resource Type:** `sling-cms/components/editor/fields/file`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *accepts* - the mime types accepted by the field, the default are: *image/*,audio/*,video/*,application/json,text/css,application/pdf*

## Hidden

Renders a hidden field

**Resource Type:** `sling-cms/components/editor/fields/hidden`

**Properties**

- *name* - the name attribute for the field
- *value* - the value of the hidden field

## Path

Renders a path picker. Currently this uses HTML5 data lists to display the dropdown of available paths.

**Resource Type:** `sling-cms/components/editor/fields/path`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *additionalParams* - any additional parameters to append to the query for further filtering
- *basePath* - the path under which to search, by default `/content`
- *titleProperty* - the property to retrieve a title for the result 
- *type* - the node type to limit the results, by default `nt:hierarchyNode`

## Repeating

Renders a repeating set of fields.

**Resource Type:** `sling-cms/components/editor/fields/repeating`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *type* - the type of field to render

## RichText

Renders a rich text editor using Summernote.

**Resource Type:** `sling-cms/components/editor/fields/richtext`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required

## Select

Renders a select field. This field can either specify a list of options in a property, as a subnode or use a script to render the options.

**Resource Type:** `sling-cms/components/editor/fields/select`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *multiple* - if set to true the field will allow multiple options to be selected
- *options* - if set, this will be used to populate the options for the select field. These should be and array of strings in the format `{LABEL}={VALUE}`
- *optionsScript* - the absolute path to a script to execute to get the select options

## Taxonomy

Renders taxonomy selection field.

**Resource Type:** `sling-cms/components/editor/fields/taxonomy`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *basePath* - the path under which to search, by default `/etc/taxonomy`

## Text

Renders an input field.

**Resource Type:** `sling-cms/components/editor/fields/text`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *defaultValue* - the default value for the field if no value has been saved already
- *type* - the type of input field, by default this is `text`


## Textarea

Renders a textarea

**Resource Type:** `sling-cms/components/editor/fields/textarea`

**Properties**

- *disabled* - if set to true the field will be disabled
- *label* - the label text for the field
- *name* - the name attribute for the field
- *required* - if set to true the field will be required
- *defaultValue* - the default value for the field if no value has been saved already