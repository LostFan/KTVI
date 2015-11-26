package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.MaterialConsumptionDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.MaterialConsumption;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.model.dto.FullRenderedService;
import org.lostfan.ktv.model.transform.RenderedServiceTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RenderedServiceEntityModel extends BaseEntityModel<RenderedService> implements EntityTableModel {

    private List<EntityField> fields;

    private List<FullEntityField> fullFields;

    private RenderedServiceTransformer transformer = new RenderedServiceTransformer();
    private MaterialConsumptionDAO materialConsumptionDAO = DAOFactory.getDefaultDAOFactory().getMaterialConsumptionDAO();

    public RenderedServiceEntityModel() {
        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("renderedService.id", EntityFieldTypes.Integer, RenderedService::getId, RenderedService::setId, false));
        this.fields.add(new EntityField("renderedService.date", EntityFieldTypes.Date, RenderedService::getDate, RenderedService::setDate));
        this.fields.add(new EntityField("subscriber", EntityFieldTypes.Subscriber, RenderedService::getSubscriberId, RenderedService::setSubscriberId));
        this.fields.add(new EntityField("service", EntityFieldTypes.Service, RenderedService::getServiceId, RenderedService::setServiceId));
        this.fields.add(new EntityField("renderedService.price", EntityFieldTypes.Integer, RenderedService::getPrice, RenderedService::setPrice));

        this.fullFields = new ArrayList<>();

        FullEntityField materialConsumptionField = new FullEntityField("materialConsumption", EntityFieldTypes.MaterialConsumption, FullRenderedService::getMaterialConsumption, FullRenderedService::setMaterialConsumption, MaterialConsumption::new);
        materialConsumptionField.setEntityFields(MainModel.getMaterialConsumptionEntityModel().getFields().stream().filter(e -> !e.getTitleKey().equals("renderedService")).filter(e -> !e.getTitleKey().equals("materialConsumption.id")).collect(Collectors.toList()));
        this.fullFields.add(materialConsumptionField);
    }

    @Override
    public List<EntityModel> getEntityModels() {
        List<EntityModel> entityModels = new ArrayList<>();
        entityModels.add(MainModel.getServiceEntityModel());
        entityModels.add(MainModel.getSubscriberEntityModel());
        return entityModels;
    }

    @Override
    public FullRenderedService getFullEntity(int id) {
        FullRenderedService dto = transformer.transformTo(getEntity(id));
        dto.setMaterialConsumption(MainModel.getMaterialConsumptionEntityModel().getListByForeignKey(id));
        return dto;
    }

    @Override
    public String getEntityName() {
        return "renderedService";
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public List<FullEntityField> getFullFields() {
        return this.fullFields;
    }

    @Override
    public List<RenderedService> getListByForeignKey(Integer foreignKey) {
        return null;
    }

    @Override
    public String getEntityNameKey() {
        return "renderedServices";
    }

    @Override
    public Class getEntityClass() {
        return RenderedService.class;
    }

    @Override
    public List<EntityModel> getTableModels() {
        if(this.entityTableModels == null) {
            this.entityTableModels = new ArrayList<>();
            EntityModel entityModel = MainModel.getMaterialConsumptionEntityModel();
            entityModel.setParentModel(this);
            this.entityTableModels.add(entityModel);
        }
        return this.entityTableModels;
    }

    @Override
    public void save(RenderedService entity) {
        if (entity.getId() == null) {
            getDao().save(entity);
            if(entity instanceof FullRenderedService)  {
                FullRenderedService fullRenderedService = (FullRenderedService) entity;
                for (MaterialConsumption materialConsumption : fullRenderedService.getMaterialConsumption()) {
                    materialConsumption.setRenderedServiceId(fullRenderedService.getId());
                    materialConsumptionDAO.save(materialConsumption);
                }
            }
        } else {
            getDao().update(entity);
            if(entity instanceof FullRenderedService) {
                FullRenderedService fullRenderedService = (FullRenderedService) entity;
                List<MaterialConsumption> materialConsumptionList = materialConsumptionDAO.getMaterialConsumptionsByRenderedServiceId(fullRenderedService.getId());
                for (MaterialConsumption materialConsumption : materialConsumptionList) {
                    if(!fullRenderedService.getMaterialConsumption().contains(materialConsumption)) {
                        materialConsumptionDAO.delete(materialConsumption.getId());
                    }
                }
                for (MaterialConsumption materialConsumption : fullRenderedService.getMaterialConsumption()) {
                    if(materialConsumption.getId() != null) {
                        materialConsumptionDAO.update(materialConsumption);
                    } else {
                        materialConsumption.setRenderedServiceId(fullRenderedService.getId());
                        materialConsumptionDAO.save(materialConsumption);
                    }
                }
            }
        }
        updateEntitiesList();
    }

    @Override
    protected EntityDAO<RenderedService> getDao() {
        return DAOFactory.getDefaultDAOFactory().getRenderedServiceDAO();
    }

    @Override
    public RenderedService createNewEntity() {
        return new RenderedService();
    }

    @Override
    public FullRenderedService buildDTO(RenderedService service, Map<String, List<Entity>> map) {
        return transformer.transformTo(service, map);
    }
}
