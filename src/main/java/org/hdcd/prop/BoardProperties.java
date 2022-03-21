package org.hdcd.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("org.hdcd")
public class BoardProperties {

	private String uploadPath;
	
}
