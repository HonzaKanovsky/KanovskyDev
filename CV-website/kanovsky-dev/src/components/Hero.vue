<script lang="ts" setup>
import photo from '@/assets/img/Photo-placeholder-hd.png'
import { RouterLink } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

</script>

<template>
    <section>
        <div>
            <div>
                <div>
                    BACK-END · FRONT-END
                </div>
                <p>
                    {{ t('hero.subtitle') }}
                </p>
                <h1>
                    {{ t('hero.title') }}
                </h1>
                <div>
                    <RouterLink to="/resume">
                        {{ t('hero.resumeButton') }}
                    </RouterLink>
                    <RouterLink to="/portfolio">
                        {{ t('hero.portfolioButton') }}
                    </RouterLink>
                </div>
            </div>
            <div>
                <div>
                    <img :src="photo">
                </div>
            </div>
        </div>
    </section>
</template>
