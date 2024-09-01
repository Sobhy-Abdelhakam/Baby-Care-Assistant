package dev.sobhy.babycareassistant.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sobhy.babycareassistant.alarm.data.repository.AlarmManagerRepositoryImpl
import dev.sobhy.babycareassistant.alarm.domain.repository.AlarmManagerRepository
import dev.sobhy.babycareassistant.authentication.domain.repository.AuthRepository
import dev.sobhy.babycareassistant.breastfeeding.data.repository.FeedingRepository
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.DeleteFeedingUseCase
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.GetFeedingByIdUseCase
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.GetFeedingUseCase
import dev.sobhy.babycareassistant.breastfeeding.domain.usecases.SaveOrUpdateFeedingUseCase
import dev.sobhy.babycareassistant.diapers.data.repository.DiapersRepository
import dev.sobhy.babycareassistant.diapers.domain.usecase.DeleteDiaperUseCase
import dev.sobhy.babycareassistant.diapers.domain.usecase.GetDiaperByIdUseCase
import dev.sobhy.babycareassistant.diapers.domain.usecase.GetDiapersUseCase
import dev.sobhy.babycareassistant.diapers.domain.usecase.SaveOrUpdateDiaperUseCase
import dev.sobhy.babycareassistant.growth.data.repository.GrowthRepository
import dev.sobhy.babycareassistant.growth.domain.usecases.DeleteGrowthUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.GetGrowthByIdUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.GetGrowthUseCase
import dev.sobhy.babycareassistant.growth.domain.usecases.SaveOrUpdateGrowthUseCase
import dev.sobhy.babycareassistant.healthinfo.data.repository.HealthInfoRepository
import dev.sobhy.babycareassistant.healthinfo.usecases.DeleteHealthInfoUseCase
import dev.sobhy.babycareassistant.healthinfo.usecases.GetHealthInfoById
import dev.sobhy.babycareassistant.healthinfo.usecases.GetHealthInfoUseCase
import dev.sobhy.babycareassistant.vaccination.data.repository.VaccinationRepository
import dev.sobhy.babycareassistant.healthinfo.usecases.SaveHealthInfoUseCase
import dev.sobhy.babycareassistant.sleep.data.repository.SleepRepository
import dev.sobhy.babycareassistant.sleep.domain.usecases.DeleteSleepUseCase
import dev.sobhy.babycareassistant.sleep.domain.usecases.GetSleepByIdUseCase
import dev.sobhy.babycareassistant.sleep.domain.usecases.GetSleepUseCase
import dev.sobhy.babycareassistant.sleep.domain.usecases.SaveOrUpdateSleepUseCase
import dev.sobhy.babycareassistant.vaccination.domain.usecases.GetVaccinationsUseCase
import dev.sobhy.babycareassistant.vaccination.domain.usecases.SaveVaccineUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAlarmManagerRepository(@ApplicationContext context: Context): AlarmManagerRepository {
        return AlarmManagerRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): AuthRepository {
        return AuthRepository(
            auth = auth,
            db = firestore,
            storage = storage
        )
    }
    @Provides
    @Singleton
    fun provideVaccineRepo(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        alarmManagerRepository: AlarmManagerRepository
    ) : VaccinationRepository {
        return VaccinationRepository(
            firebaseAuth = auth,
            firestore = firestore,
            alarmManagerRepository = alarmManagerRepository
        )
    }
    @Provides
    @Singleton
    fun provideGetVaccinationUseCase(vaccinationRepository: VaccinationRepository): GetVaccinationsUseCase {
        return GetVaccinationsUseCase(vaccinationRepository)
    }
    @Provides
    @Singleton
    fun provideSaveVaccineUseCase(vaccinationRepository: VaccinationRepository): SaveVaccineUseCase {
        return SaveVaccineUseCase(vaccinationRepository)
    }
    @Provides
    @Singleton
    fun provideGrowthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): GrowthRepository{
        return GrowthRepository(auth, firestore)
    }
    @Provides
    @Singleton
    fun provideGetGrowthUseCase(growthRepository: GrowthRepository): GetGrowthUseCase {
        return GetGrowthUseCase(growthRepository)
    }
    @Provides
    @Singleton
    fun provideSaveGrowthUseCase(growthRepository: GrowthRepository): SaveOrUpdateGrowthUseCase {
        return SaveOrUpdateGrowthUseCase(growthRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteGrowthUseCase(growthRepository: GrowthRepository): DeleteGrowthUseCase {
        return DeleteGrowthUseCase(growthRepository)
    }
    @Provides
    @Singleton
    fun provideGetGrowthByIdUseCase(growthRepository: GrowthRepository): GetGrowthByIdUseCase {
        return GetGrowthByIdUseCase(growthRepository)
    }
    @Provides
    @Singleton
    fun provideSleepRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): SleepRepository{
        return SleepRepository(auth, firestore)
    }
    @Provides
    @Singleton
    fun provideGetSleepUseCase(sleepRepository: SleepRepository): GetSleepUseCase {
        return GetSleepUseCase(sleepRepository)
    }
    @Provides
    @Singleton
    fun provideSaveSleepUseCase(sleepRepository: SleepRepository): SaveOrUpdateSleepUseCase {
        return SaveOrUpdateSleepUseCase(sleepRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteSleepUseCase(sleepRepository: SleepRepository): DeleteSleepUseCase {
        return DeleteSleepUseCase(sleepRepository)
    }
    @Provides
    @Singleton
    fun provideGetSleepByIdUseCase(sleepRepository: SleepRepository): GetSleepByIdUseCase {
        return GetSleepByIdUseCase(sleepRepository)
    }
    @Provides
    @Singleton
    fun provideDiapersRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        alarmManagerRepository: AlarmManagerRepository
    ): DiapersRepository{
        return DiapersRepository(auth, firestore, alarmManagerRepository)
    }
    @Provides
    @Singleton
    fun provideGetDiapersUseCase(diapersRepository: DiapersRepository): GetDiapersUseCase {
        return GetDiapersUseCase(diapersRepository)
    }
    @Provides
    @Singleton
    fun provideSaveDiapersUseCase(diapersRepository: DiapersRepository): SaveOrUpdateDiaperUseCase {
        return SaveOrUpdateDiaperUseCase(diapersRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteDiapersUseCase(diapersRepository: DiapersRepository): DeleteDiaperUseCase {
        return DeleteDiaperUseCase(diapersRepository)
    }
    @Provides
    @Singleton
    fun provideGetDiapersByIdUseCase(diapersRepository: DiapersRepository): GetDiaperByIdUseCase {
        return GetDiaperByIdUseCase(diapersRepository)
    }

    @Provides
    @Singleton
    fun provideHealthInfoRepo(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ) : HealthInfoRepository {
        return HealthInfoRepository(
            firebaseAuth = auth,
            firestore = firestore,
        )
    }
    @Provides
    @Singleton
    fun provideGetHealthInfoUseCase(healthInfoRepository: HealthInfoRepository): GetHealthInfoUseCase {
        return GetHealthInfoUseCase(healthInfoRepository)
    }
    @Provides
    @Singleton
    fun provideSaveHealthInfoUseCase(healthInfoRepository: HealthInfoRepository): SaveHealthInfoUseCase {
        return SaveHealthInfoUseCase(healthInfoRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteHealthInfoUseCase(healthInfoRepository: HealthInfoRepository): DeleteHealthInfoUseCase {
        return DeleteHealthInfoUseCase(healthInfoRepository)
    }
    @Provides
    @Singleton
    fun provideGetHealthInfoByIdUseCase(healthInfoRepository: HealthInfoRepository): GetHealthInfoById {
        return GetHealthInfoById(healthInfoRepository)
    }

    @Provides
    @Singleton
    fun provideFeedingRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        alarmManagerRepository: AlarmManagerRepository
    ): FeedingRepository{
        return FeedingRepository(
            firebaseAuth = auth,
            firestore = firestore,
            alarmManagerRepository = alarmManagerRepository
        )
    }
    @Provides
    @Singleton
    fun provideGetFeedingUseCase(feedingRepository: FeedingRepository): GetFeedingUseCase {
        return GetFeedingUseCase(feedingRepository)
    }
    @Provides
    @Singleton
    fun provideSaveOrUpdateFeedingUseCase(feedingRepository: FeedingRepository): SaveOrUpdateFeedingUseCase {
        return SaveOrUpdateFeedingUseCase(feedingRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteFeedingUseCase(feedingRepository: FeedingRepository): DeleteFeedingUseCase {
        return DeleteFeedingUseCase(feedingRepository)
    }
    @Provides
    @Singleton
    fun provideGetFeedingByIdUseCase(feedingRepository: FeedingRepository): GetFeedingByIdUseCase {
        return GetFeedingByIdUseCase(feedingRepository)
    }
}