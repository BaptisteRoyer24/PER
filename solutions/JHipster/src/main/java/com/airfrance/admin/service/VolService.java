package com.airfrance.admin.service;

import com.airfrance.admin.domain.Vol;
import com.airfrance.admin.repository.VolRepository;
import com.airfrance.admin.service.dto.VolDTO;
import com.airfrance.admin.service.mapper.VolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.airfrance.admin.domain.Vol}.
 */
@Service
@Transactional
public class VolService {

    private static final Logger LOG = LoggerFactory.getLogger(VolService.class);

    private final VolRepository volRepository;

    private final VolMapper volMapper;

    public VolService(VolRepository volRepository, VolMapper volMapper) {
        this.volRepository = volRepository;
        this.volMapper = volMapper;
    }

    /**
     * Save a vol.
     *
     * @param volDTO the entity to save.
     * @return the persisted entity.
     */
    public VolDTO save(VolDTO volDTO) {
        LOG.debug("Request to save Vol : {}", volDTO);
        Vol vol = volMapper.toEntity(volDTO);
        vol = volRepository.save(vol);
        return volMapper.toDto(vol);
    }

    /**
     * Update a vol.
     *
     * @param volDTO the entity to save.
     * @return the persisted entity.
     */
    public VolDTO update(VolDTO volDTO) {
        LOG.debug("Request to update Vol : {}", volDTO);
        Vol vol = volMapper.toEntity(volDTO);
        vol = volRepository.save(vol);
        return volMapper.toDto(vol);
    }

    /**
     * Partially update a vol.
     *
     * @param volDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VolDTO> partialUpdate(VolDTO volDTO) {
        LOG.debug("Request to partially update Vol : {}", volDTO);

        return volRepository
            .findById(volDTO.getId())
            .map(existingVol -> {
                volMapper.partialUpdate(existingVol, volDTO);

                return existingVol;
            })
            .map(volRepository::save)
            .map(volMapper::toDto);
    }

    /**
     * Get one vol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VolDTO> findOne(Long id) {
        LOG.debug("Request to get Vol : {}", id);
        return volRepository.findById(id).map(volMapper::toDto);
    }

    /**
     * Delete the vol by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vol : {}", id);
        volRepository.deleteById(id);
    }
}
