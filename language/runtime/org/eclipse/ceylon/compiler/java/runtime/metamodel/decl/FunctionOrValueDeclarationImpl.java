/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Parameter;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.FunctionOrValueDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.NestableDeclarationImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public abstract class FunctionOrValueDeclarationImpl 
    extends NestableDeclarationImpl
    implements ceylon.language.meta.declaration.FunctionOrValueDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FunctionOrValueDeclarationImpl.class);
    
    protected Parameter parameter;

    public FunctionOrValueDeclarationImpl(org.eclipse.ceylon.model.typechecker.model.Declaration declaration) {
        super(declaration);
        this.parameter = declaration instanceof org.eclipse.ceylon.model.typechecker.model.TypedDeclaration ? Metamodel.getParameterFromTypedDeclaration((org.eclipse.ceylon.model.typechecker.model.TypedDeclaration)declaration) : null;
    }
    
    @Override
    public boolean getDefaulted(){
        return parameter == null ? false : parameter.isDefaulted();
    }
    
    @Override
    public boolean getVariadic(){
        return parameter == null ? false : parameter.isSequenced();
    }

    @Override
    public boolean getParameter(){
        return parameter != null;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
