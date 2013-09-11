package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.Class;
import ceylon.language.model.ClassModel$impl;
import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.Member$impl;
import ceylon.language.model.MemberClass$impl;
import ceylon.language.model.declaration.ClassDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedMemberClass<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.model.MemberClass<Container, Type, Arguments> {

    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedArguments;

    AppliedMemberClass(@Ignore TypeDescriptor $reifiedContainer,
                       @Ignore TypeDescriptor $reifiedType,
                       @Ignore TypeDescriptor $reifiedArguments, 
                       ProducedType producedType) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.$reifiedContainer = $reifiedContainer;
    }

    @Override
    @Ignore
    public ClassModel$impl<Type, Arguments> $ceylon$language$model$ClassModel$impl() {
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, Class<? extends Type, ? super Arguments>> $ceylon$language$model$Member$impl() {
        return null;
    }

    @Override
    @Ignore
    public MemberClass$impl<Container, Type, Arguments> $ceylon$language$model$MemberClass$impl() {
        return null;
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0) {
        return new AppliedClass<Type, Arguments>($reifiedType, $reifiedArguments, super.producedType, getContainer(), arg0);
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return -1;
    }
    
    @Override
    @TypeInfo("ceylon.language.model.declaration::ClassDeclaration")
    public ClassDeclaration getDeclaration() {
        return (ClassDeclaration) super.getDeclaration();
    }
    
    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        return (ClassOrInterface<? extends Object>) Metamodel.getAppliedMetamodel(producedType.getQualifyingType());
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMemberClass.class, $reifiedContainer, $reifiedType, $reifiedArguments);
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic() {
        return $call$variadic(empty_.$get());
    }
    
    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(
            Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(Object arg0) {
        return $call$variadic(arg0, empty_.$get());
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(Object arg0,
            Object arg1) {
        return $call$variadic(arg0, arg1, empty_.$get());
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$variadic(Object arg0,
            Object arg1, Object arg2) {
        return $call$variadic(arg0, arg1, arg2, empty_.$get());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringClassOrInterface().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.model.MemberClass == false)
            return false;
        ceylon.language.model.MemberClass<?, ?, ?> other = (ceylon.language.model.MemberClass<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringClassOrInterface().equals(other.getDeclaringClassOrInterface())
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends java.lang.Object> getContainer(){
        return getDeclaringClassOrInterface();
    }
}
