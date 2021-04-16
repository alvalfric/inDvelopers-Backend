
package ISPP.G5.INDVELOPERS.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.controllers.DeveloperController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.repositories.DeveloperSubscriptionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperSubscriptionService {

	@Autowired
	private DeveloperSubscriptionRepository	developerSubscriptionRepository;
	
	public DeveloperSubscription findByDeveloper(final Developer developer) {
		DeveloperSubscription developerSubscription = this.developerSubscriptionRepository.findByDeveloperId(developer.getId()).orElse(null);
		
		if(developerSubscription == null) {
			developerSubscription = new DeveloperSubscription();
			developerSubscription.setDeveloper(developer);
		}
		
		return developerSubscription;
	}

	public boolean checkDeveloperHasSubscription(final Developer developer) {
		boolean result = false;
		DeveloperSubscription developerSubscription = this.developerSubscriptionRepository.findByDeveloperId(developer.getId()).orElse(null);

		if (developerSubscription != null) {
			if(developerSubscription.getEndDate().isAfter(LocalDate.now()) || developerSubscription.getEndDate().isEqual(LocalDate.now())) {
				result = true;
			} 
		}

		return result;
	}
	
	public String buySubscription(final Developer developer) {
		DeveloperSubscription devSub = this.findByDeveloper(developer);
		
		if(this.checkDeveloperHasSubscription(developer)) {
			LocalDate devSubEndDate = devSub.getEndDate();
			devSub.setEndDate(devSubEndDate.plusMonths(1));
		} else {
			devSub.setStartDate(LocalDate.now());
			devSub.setEndDate(LocalDate.now().plusMonths(1));
		}
		this.developerSubscriptionRepository.save(devSub);
		
		return "Buyed subscription for user " + developer.getUsername() +" with end date "+devSub.getEndDate();
	}
}
