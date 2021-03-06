/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import ceylon.language.impl.BaseIterator;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

/** 
 * A non-user-visible class that helps create iterators in 
 * generated bytecode.
 * 
 * @author Enrique Zamudio
 */
public abstract class AbstractIterator<Element> 
extends BaseIterator<Element> {
    
    public AbstractIterator(@Ignore TypeDescriptor $reifiedElement) {
    	super($reifiedElement);
    }
    
}
