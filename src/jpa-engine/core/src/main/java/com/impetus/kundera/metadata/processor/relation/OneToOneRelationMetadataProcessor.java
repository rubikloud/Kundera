/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.metadata.processor.relation;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.AssociationOverride;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.impetus.kundera.loader.MetamodelLoaderException;
import com.impetus.kundera.metadata.KunderaMetadataManager;
import com.impetus.kundera.metadata.model.EntityMetadata;
import com.impetus.kundera.metadata.model.Relation;
import com.impetus.kundera.metadata.model.attributes.AbstractAttribute;
import com.impetus.kundera.metadata.processor.AbstractEntityFieldProcessor;
import com.impetus.kundera.metadata.validator.EntityValidatorImpl;
import com.impetus.kundera.persistence.EntityManagerFactoryImpl.KunderaMetadata;

/**
 * The Class OneToOneRelationMetadataProcessor.
 * 
 * @author Amresh Singh
 */
public class OneToOneRelationMetadataProcessor extends AbstractEntityFieldProcessor implements
        RelationMetadataProcessor
{

    /**
     * Instantiates a new one to one relation metadata processor.
     */
    public OneToOneRelationMetadataProcessor(KunderaMetadata kunderaMetadata)
    {
        super(kunderaMetadata);
        validator = new EntityValidatorImpl();
    }

    @Override
    public void addRelationIntoMetadata(Field relationField, EntityMetadata metadata)
    {
        // taking field's type as foreign entity, ignoring "targetEntity"
        Class<?> targetEntity = relationField.getType();
        validate(targetEntity);

        // TODO: Add code to check whether this entity has already been
        // validated, at all places below

        OneToOne oneToOneAnn = relationField.getAnnotation(OneToOne.class);

        boolean isJoinedByPK = relationField.isAnnotationPresent(PrimaryKeyJoinColumn.class);
        boolean isJoinedByFK = relationField.isAnnotationPresent(JoinColumn.class);
        boolean isJoinedByTable = relationField.isAnnotationPresent(JoinTable.class);

        Relation relation = new Relation(relationField, targetEntity, null, oneToOneAnn.fetch(),
                Arrays.asList(oneToOneAnn.cascade()), oneToOneAnn.optional(), oneToOneAnn.mappedBy(),
                Relation.ForeignKey.ONE_TO_ONE);

        if (relationField.isAnnotationPresent(AssociationOverride.class))
        {
            AssociationOverride annotation = relationField.getAnnotation(AssociationOverride.class);
            JoinColumn[] joinColumns = annotation.joinColumns();

            validateJoinColumns(joinColumns);

            relation.setJoinColumnName(joinColumns[0].name());

            JoinTable joinTable = annotation.joinTable();
            onJoinTable(joinTable);
        }
        else if (isJoinedByPK)
        {

            relation.setJoinedByPrimaryKey(true);
            EntityMetadata joinClassMetadata = KunderaMetadataManager.getEntityMetadata(kunderaMetadata,
                    targetEntity.getClass());
            relation.setJoinColumnName(joinClassMetadata != null ? ((AbstractAttribute) joinClassMetadata
                    .getIdAttribute()).getJPAColumnName() : null);
        }
        else if (isJoinedByFK)
        {
            JoinColumn joinColumnAnn = relationField.getAnnotation(JoinColumn.class);
            relation.setJoinColumnName(joinColumnAnn.name());
        }
        else if (isJoinedByTable)
        {
            throw new UnsupportedOperationException("@JoinTable not supported for one to one association");
        }

        relation.setBiDirectionalField(metadata.getEntityClazz());
        metadata.addRelation(relationField.getName(), relation);
    }

    private void onJoinTable(JoinTable joinTable)
    {
        if (joinTable != null)
        {
            throw new UnsupportedOperationException("@JoinTable not supported for many to one association");
        }
    }

    private void validateJoinColumns(JoinColumn[] joinColumns)
    {
        if (joinColumns.length > 1)
        {
            throw new UnsupportedOperationException("More than one join columns are not supported.");
        }
    }

    @Override
    public void process(Class<?> clazz, EntityMetadata metadata)
    {
        throw new MetamodelLoaderException("Method call not applicable for Relation processors");
    }
}