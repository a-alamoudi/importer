/* Copyright 2014 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.importer.handler.filter;


/**
 * Tells the collector that a filter is of "OnMatch" type.  This means,
 * if one or more filters of type "include" exists in a set of filters, 
 * at least one of them must be matched for a document (or other object)
 * to be "included".  Only one filter of type "exclude" needs to be 
 * matched or the document (or other object) to be excluded.
 * Filters of type "exclude" have precedence over includes.
 * @author Pascal Essiembre
 */
public interface IOnMatchFilter {

    /**
     * Gets the the on match action (exclude or include).
     * @return on match (exclude or include)
     */
    OnMatch getOnMatch();
}