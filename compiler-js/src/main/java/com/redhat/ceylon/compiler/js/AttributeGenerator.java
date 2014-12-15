package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;

public class AttributeGenerator {

    static void getter(final Tree.AttributeGetterDefinition that, final GenerateJsVisitor gen) {
        gen.beginBlock();
        gen.initSelf(that);
        gen.visitStatements(that.getBlock().getStatements());
        gen.endBlock();
    }

    static void setter(final Tree.AttributeSetterDefinition that, final GenerateJsVisitor gen) {
        if (that.getSpecifierExpression() == null) {
            gen.beginBlock();
            gen.initSelf(that);
            gen.visitStatements(that.getBlock().getStatements());
            gen.endBlock();
        } else {
            gen.out("{");
            gen.initSelf(that);
            gen.out("return ");
            if (!gen.isNaturalLiteral(that.getSpecifierExpression().getExpression().getTerm())) {
                that.getSpecifierExpression().visit(gen);
            }
            gen.out(";}");
        }
    }

    static void generateAttributeGetter(final Tree.AnyAttribute attributeNode, final MethodOrValue decl,
            final Tree.SpecifierOrInitializerExpression expr, final String param, final GenerateJsVisitor gen,
            final Set<Declaration> directAccess) {
        final boolean asProp = gen.defineAsProperty(decl) && (gen.isCaptured(decl) || decl.isToplevel());
        final String varName;
        if (asProp) {
            varName = decl.isShared() ? gen.getNames().name(decl) + "_" : gen.getNames().privateName(decl);
        } else {
            varName = gen.getNames().name(decl);
        }
        final boolean initVal = expr != null && decl.isToplevel();
        gen.out("var ", varName);
        if (expr != null) {
            if (initVal) {
                gen.out(";function $valinit$", varName, "(){");
                gen.out("if(", varName, "===", gen.getClAlias(), "INIT$)");
                gen.generateThrow(gen.getClAlias()+"InitializationError",
                        "Cyclic initialization trying to read the value of '" +
                        decl.getName() + "' before it was set", attributeNode);
                gen.endLine(true);
                gen.out("if(", varName, "===undefined){",
                        varName, "=", gen.getClAlias(), "INIT$;", varName, "=");
            } else {
                gen.out("=");
            }
            int boxType = gen.boxStart(expr.getExpression().getTerm());
            if (gen.isInDynamicBlock() && Util.isTypeUnknown(expr.getExpression().getTypeModel())
                    && !Util.isTypeUnknown(decl.getType())) {
                TypeUtils.generateDynamicCheck(expr.getExpression(), decl.getType(), gen, false,
                        expr.getExpression().getTypeModel().getTypeArguments());
            } else {
                expr.visit(gen);
            }
            if (boxType == 4) {
                //Pass Callable argument types
                gen.out(",");
                if (decl instanceof Method) {
                    //Add parameters
                    TypeUtils.encodeParameterListForRuntime(attributeNode, ((Method)decl).getParameterLists().get(0), gen);
                } else {
                    //Type of value must be Callable
                    //And the Args Type Parameters is a Tuple
                    TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(attributeNode,
                            expr.getExpression().getTypeModel(), gen);
                }
                gen.out(",");
                TypeUtils.printTypeArguments(expr, expr.getExpression().getTypeModel().getTypeArguments(), gen, false,
                        expr.getExpression().getTypeModel().getVarianceOverrides());
            }
            gen.boxUnboxEnd(boxType);
            if (initVal) {
                gen.out("};return ", varName, ";};$valinit$", varName, "()");
            }
        } else if (param != null) {
            gen.out("=", param);
        }
        gen.endLine(true);
        if (decl instanceof Method) {
            if (decl.isClassOrInterfaceMember() && gen.isCaptured(decl)) {
                gen.beginNewLine();
                gen.outerSelf(decl);
                gen.out(".", varName, "=", varName);
                gen.endLine(true);
            }
        } else {
            if (gen.isCaptured(decl) || decl.isToplevel()) {
                final boolean isLate = decl.isLate();
                if (asProp) {
                    gen.out(gen.getClAlias(), "atr$(");
                    gen.outerSelf(decl);
                    gen.out(",'", gen.getNames().name(decl), "',function(){");
                    if (isLate) {
                        gen.generateUnitializedAttributeReadCheck(varName, gen.getNames().name(decl));
                    }
                    if (initVal) {
                        gen.out("return $valinit$", varName, "();}");
                    } else {
                        gen.out("return ", varName, ";}");
                    }
                    if (decl.isVariable() || isLate) {
                        final String par = gen.getNames().createTempVariable();
                        gen.out(",function(", par, "){");
                        gen.generateImmutableAttributeReassignmentCheck(decl, varName, gen.getNames().name(decl));
                        gen.out("return ", varName, "=", par, ";}");
                    } else {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    if (attributeNode == null) {
                        TypeUtils.encodeForRuntime(expr, decl, gen);
                    } else {
                        TypeUtils.encodeForRuntime(decl, attributeNode.getAnnotationList(), gen);
                    }
                    gen.out(")");
                    gen.endLine(true);
                }
                else {
                    gen.out(GenerateJsVisitor.function, gen.getNames().getter(decl),"(){return ");
                    if (initVal) {
                        gen.out("$valinit$", varName, "();}");
                    } else {
                        gen.out(varName, ";}");
                    }
                    gen.endLine();
                    gen.shareGetter(decl);
                }
            } else {
                if (decl.isMember() && gen.qualify(expr, decl)) {
                    gen.out(varName, "=", varName, ";");
                }
                directAccess.add(decl);
            }
        }
    }

    static void generateAttributeSetter(final Tree.AnyAttribute that, final MethodOrValue d, final GenerateJsVisitor gen) {
        final String varName = gen.getNames().name(d);
        String paramVarName = gen.getNames().createTempVariable();
        gen.out(GenerateJsVisitor.function, gen.getNames().setter(d), "(", paramVarName, "){");
        gen.generateImmutableAttributeReassignmentCheck(d, varName, gen.getNames().name(d));
        gen.out("return ", varName, "=", paramVarName, ";}");
        gen.endLine(true);
        gen.shareSetter(d);
    }

    static void addGetterAndSetterToPrototype(final TypeDeclaration outer, final Tree.AttributeDeclaration that,
            final GenerateJsVisitor gen) {
        final Value d = that.getDeclarationModel();
        if (!gen.opts.isOptimize()||d.isToplevel()||d.getTypeDeclaration()==null) return;
        gen.comment(that);
        final String atname = d.isShared() ? gen.getNames().name(d) + "_" :
            gen.getNames().privateName(d);
        if (d.isFormal()) {
            gen.generateAttributeMetamodel(that, false, false);
        } else if (that.getSpecifierOrInitializerExpression() == null) {
            gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
            gen.out("{");
            if (gen.shouldStitch(d)) {
                if (!gen.stitchNative(d, that)) {
                    gen.out("throw new Error('MISSING native code for " + d.getQualifiedNameString() + "');");
                }
            } else {
                //Just return the private value #451
                if (d.isLate()) {
                    gen.generateUnitializedAttributeReadCheck("this."+atname, gen.getNames().name(d));
                }
                gen.out("return this.", atname , ";");
            }
            gen.out("},");
            Tree.AttributeSetterDefinition setterDef = null;
            boolean setterDefined=false;
            if (d.isVariable() || d.isLate()) {
                setterDef = gen.associatedSetterDefinition(d);
                if (setterDef == null) {
                    setterDefined = true;
                    final String par = gen.getNames().createTempVariable();
                    gen.out("function(", par, "){");
                    gen.generateImmutableAttributeReassignmentCheck(d, "this."+atname, gen.getNames().name(d));
                    gen.out("return this.", atname, "=", par, ";}");
                } else if (!gen.stitchNative(setterDef.getDeclarationModel(), that)) {
                    gen.out("function(", gen.getNames().name(
                            setterDef.getDeclarationModel().getParameter()), ")");
                    setter(setterDef, gen);
                    setterDefined = true;
                }
            }
            if (!setterDefined) {
                gen.out("undefined");
            }
            gen.out(",");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
            if (setterDef != null) {
                gen.out(",");
                TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), that.getAnnotationList(), gen);
            }
            gen.out(")");
            gen.endLine(true);
        } else {
            com.redhat.ceylon.compiler.typechecker.model.Parameter param = null;
            if (d.isParameter()) {
                param = ((Functional)d.getContainer()).getParameter(d.getName());
            }
            if ((that.getSpecifierOrInitializerExpression() != null) || d.isVariable()
                        || param != null || d.isLate()) {
                if (that.getSpecifierOrInitializerExpression()
                                instanceof LazySpecifierExpression) {
                    // attribute is defined by a lazy expression ("=>" syntax)
                    gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
                    gen.beginBlock();
                    gen.initSelf(that);
                    gen.out("return ");
                    Expression expr = that.getSpecifierOrInitializerExpression().getExpression();
                    if (!gen.isNaturalLiteral(expr.getTerm())) {
                        final int boxType = gen.boxStart(expr.getTerm());
                        expr.visit(gen);
                        gen.endLine(true);
                        if (boxType == 4) gen.out("/*TODO: callable targs 3*/");
                        gen.boxUnboxEnd(boxType);
                    }
                    gen.endBlock();
                    Tree.AttributeSetterDefinition setterDef = null;
                    if (d.isVariable()) {
                        setterDef = gen.associatedSetterDefinition(d);
                        if (setterDef != null) {
                            gen.out(",function(", gen.getNames().name(setterDef.getDeclarationModel().getParameter()), ")");
                            setter(setterDef, gen);
                        }
                    }
                    if (setterDef == null) {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
                    if (setterDef != null) {
                        gen.out(",");
                        TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), that.getAnnotationList(), gen);
                    }
                    gen.out(")");
                    gen.endLine(true);
                }
                else {
                    final String privname = param == null ? gen.getNames().privateName(d) : gen.getNames().name(param)+"_";
                    gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
                    gen.out("{");
                    if (d.isLate()) {
                        gen.generateUnitializedAttributeReadCheck("this."+privname, gen.getNames().name(d));
                    }
                    gen.out("return this.", privname, ";}");
                    if (d.isVariable() || d.isLate()) {
                        final String pname = gen.getNames().createTempVariable();
                        gen.out(",function(", pname, "){");
                        gen.generateImmutableAttributeReassignmentCheck(d, "this."+privname, gen.getNames().name(d));
                        gen.out("return this.", privname,
                                "=", pname, ";}");
                    } else {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
                    gen.out(")");
                    gen.endLine(true);
                }
            }
        }
    }

}
