<script lang="ts" setup>
import photo from '@/assets/img/Photo-placeholder-hd.png'
import { RouterLink } from 'vue-router'
import { useI18n } from 'vue-i18n'

interface HeroMessages {
  hero: {
    title: string
    subtitle: string
  }
}

const { t } = useI18n<HeroMessages>()

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
                        Resume
                    </RouterLink>
                    <RouterLink to="/projects">
                        Projects
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
