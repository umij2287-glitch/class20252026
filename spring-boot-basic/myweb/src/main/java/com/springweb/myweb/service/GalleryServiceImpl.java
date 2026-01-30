package com.springweb.myweb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springweb.myweb.dto.GalleryDto;
import com.springweb.myweb.entity.GalleryEntity;
import com.springweb.myweb.repository.GalleryRepository;


@Service // @Service 어노테이션을 붙여서 서비스 클래스임을 명시. @Controller, @Repository 와 함께 사용됨.
public class GalleryServiceImpl implements GalleryService {
	
	// DI(Dependency Injection) : 생성자 의존 주입.
	private GalleryRepository galleryRepository;
	public GalleryServiceImpl(GalleryRepository galleryRepository) {
		this.galleryRepository = galleryRepository;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class) // 예외 발생 시 롤백 처리.
	public void registerGallery(GalleryDto galleryDto) {
		
		GalleryEntity galleryEntity = GalleryEntity.toEntity(galleryDto);
		galleryRepository.save(galleryEntity);
		
		// int x = 10/0; // 강제로 예외 발생시키기.
	}

	@Override
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정. 성능 최적화.
	public List<GalleryDto> findAll() {
		
		ArrayList<GalleryDto> galleryDtoList = new ArrayList<>();
		List<GalleryEntity> galleryEntityList = galleryRepository.findAll();
		for (GalleryEntity galleryEntity : galleryEntityList) {
			GalleryDto galleryDto = galleryEntity.toDto();
			galleryDtoList.add(galleryDto);
		}
		return galleryDtoList;
	}

	@Override
	@Transactional(readOnly = true)
	public GalleryDto findByIdx(int idx) {
		
		Optional<GalleryEntity> galleryEntity = galleryRepository.findById(idx);
		GalleryEntity galleryEntity2 = galleryEntity.orElseGet(() -> null); // 있으면 값을, 없으면 lambda 식의 값을 반환.
		// GalleryEntity galleryEntity = galleryEntity.orElse(null);
		return galleryEntity2.toDto();
		
	}

	@Override
	public void update(GalleryDto galleryDto) {
		GalleryEntity entity = galleryRepository.findById(galleryDto.getIdx()).orElseGet(null);
		entity.setTitle(galleryDto.getTitle());
		entity.setDescription(galleryDto.getDescription());
		entity.setOriginalFileName(galleryDto.getOriginalFileName());
		entity.setSavedFileName(galleryDto.getSavedFileName());
		entity.setRegisterDate(galleryDto.getRegisterDate());
		entity.setDeleted(galleryDto.isDeleted());
		galleryRepository.save(entity);
	}
}
