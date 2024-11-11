package com.tugasakhir.ideapedia.service;

import com.tugasakhir.ideapedia.core.IService;
import com.tugasakhir.ideapedia.dto.response.RespUnitKerjaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValUnitKerjaDTO;
import com.tugasakhir.ideapedia.model.UnitKerja;
import com.tugasakhir.ideapedia.model.User;
import com.tugasakhir.ideapedia.repo.UnitKerjaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.util.GlobalFunction;
import com.tugasakhir.ideapedia.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UnitKerjaService implements IService<UnitKerja> {

    @Autowired
    private UnitKerjaRepo unitKerjaRepo;

    @Autowired
    private UserRepo userRepo;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();

    // Simplify the getCurrentUserId function to avoid unnecessary code
    public Long getCurrentUserId(HttpServletRequest request) {
        // Get username from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();

        // Find the user by username
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(User::getId).orElse(null); // Return user ID if present
    }

    @Override
    @Transactional
    public ResponseEntity<Object> save(UnitKerja unitKerja, HttpServletRequest request) {
        try {
            unitKerja.setCreatedAt(LocalDateTime.now());
            unitKerja.setCreatedBy(getCurrentUserId(request));

            unitKerjaRepo.save(unitKerja);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", unitKerja.getId());
            responseData.put("unitName", unitKerja.getUnitName());
            responseData.put("isAdmin", unitKerja.getIsAdmin());
            responseData.put("createdAt", unitKerja.getCreatedAt());
            responseData.put("createdBy", unitKerja.getCreatedBy());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    GlobalFunction.dataUnitKerjaBerhasilDisimpan(request, responseData)
            );

        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logger instead of printing stacktrace
            return GlobalFunction.dataGagalDisimpan("SUK004001", request);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, UnitKerja unitKerja, HttpServletRequest request) {
        try{
            Optional<UnitKerja> optionalUnitKerja = unitKerjaRepo.findById(id);
            if(!optionalUnitKerja.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            UnitKerja unitKerjaNext = optionalUnitKerja.get();
            unitKerjaNext.setUnitName(unitKerja.getUnitName());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<User> optionalUser = userRepo.findById(id);
            if(!optionalUser.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            userRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<UnitKerja> page = unitKerjaRepo.findAll(pageable);
        List<UnitKerja> list = page.getContent();

        if (list.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespUnitKerjaDTO(list),
                page,
                null, null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<UnitKerja> optionalUnitKerja = unitKerjaRepo.findById(id);
        if (!optionalUnitKerja.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        UnitKerja unitKerja = optionalUnitKerja.get();
        RespUnitKerjaDTO responseDTO = modelMapper.map(unitKerja, RespUnitKerjaDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<UnitKerja> page;
        switch (columnName) {
            case "unitName":
                page = unitKerjaRepo.findByUnitNameContainingIgnoreCase(pageable, value);
                break;
            default:
                page = unitKerjaRepo.findAll(pageable);
        }

        List<UnitKerja> list = page.getContent();
        if (list.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespUnitKerjaDTO(list),
                page,
                columnName, value
        );
    }

    // Convert DTO to entity
    public UnitKerja convertToEntity(ValUnitKerjaDTO valUnitKerjaDTO) {
        return modelMapper.map(valUnitKerjaDTO, UnitKerja.class);
    }

    // Convert list of UnitKerja entities to DTOs
    public List<RespUnitKerjaDTO> convertToListRespUnitKerjaDTO(List<UnitKerja> unitKerjas) {
        return modelMapper.map(unitKerjas, new TypeToken<List<RespUnitKerjaDTO>>(){}.getType());
    }
}
